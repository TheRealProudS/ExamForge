import Foundation
import Combine

@MainActor
final class QuizViewModel: ObservableObject {

    // MARK: - Published State

    @Published var questions: [Question] = []
    @Published var currentIndex: Int = 0
    @Published var currentQuestion: Question?
    @Published var selectedIndices: Set<Int> = []
    @Published var isAnswerChecked: Bool = false
    @Published var isCorrect: Bool = false
    @Published var quizFinished: Bool = false
    @Published var sessionId: Int64 = -1
    @Published var timerSeconds: Int = 0
    @Published var isLoading: Bool = true

    private let repository: QuestionRepository
    private var params: QuizParams
    private var timerTask: Task<Void, Never>?
    private var questionStartTime: Date = Date()
    private var collectedAnswers: [UserAnswer] = []
    private var activeSessionId: Int64 = -1
    private let examTimeLimitSeconds = 90 * 60     // 5400 s

    var isExamMode: Bool { params.sessionType == .EXAM_SIMULATION }

    init(params: QuizParams, repository: QuestionRepository) {
        self.params = params
        self.repository = repository
    }

    // MARK: - Initialisation

    func start() {
        Task {
            isLoading = true
            let questionList: [Question]
            switch params.sessionType {
            case .LERNFELD:
                questionList = await repository.getQuestionsForLernfeld(lernfeld: params.lernfeld, fachrichtung: params.fachrichtung, limit: 20).shuffled()
            case .EXAM_SIMULATION:
                questionList = await repository.getQuestionsForExam(examType: params.examType, fachrichtung: params.fachrichtung, limit: 40).shuffled()
            case .QUICK_QUIZ:
                questionList = await repository.getRandomQuestions(fachrichtung: params.fachrichtung, limit: 10).shuffled()
            case .BOOKMARK_REVIEW:
                questionList = (await repository.getBookmarkedQuestions()).shuffled()
            }

            guard !questionList.isEmpty else {
                isLoading = false
                return
            }

            questions = questionList
            currentIndex = 0
            currentQuestion = questionList.first
            questionStartTime = Date()

            let session = Session(
                id: PersistenceController.shared.generateId(),
                sessionType: params.sessionType,
                fachrichtung: params.fachrichtung,
                examType: params.sessionType == .EXAM_SIMULATION ? params.examType : nil,
                lernfeld: params.sessionType == .LERNFELD ? params.lernfeld : nil,
                totalQuestions: questionList.count,
                correctAnswers: 0, durationSeconds: 0, completedAt: nil,
                startedAt: Date(), questionIds: []
            )
            activeSessionId = await repository.startSession(session)
            sessionId = activeSessionId

            if params.sessionType == .EXAM_SIMULATION {
                timerSeconds = examTimeLimitSeconds
                startTimer()
            }
            isLoading = false
        }
    }

    // MARK: - User Actions

    func toggleOption(_ index: Int) {
        guard !isAnswerChecked else { return }
        if currentQuestion?.correctAnswerIndices.count == 1 {
            selectedIndices = [index]
        } else {
            var updated = selectedIndices
            if updated.contains(index) { updated.remove(index) } else { updated.insert(index) }
            selectedIndices = updated
        }
    }

    func checkAnswer() {
        guard let question = currentQuestion else { return }
        let correct = question.correctAnswerIndices.count == 1
            ? selectedIndices == Set(question.correctAnswerIndices)
            : selectedIndices == Set(question.correctAnswerIndices)

        isCorrect = correct
        isAnswerChecked = true

        let timeSpent = Int(Date().timeIntervalSince(questionStartTime))
        collectedAnswers.append(UserAnswer(
            id: PersistenceController.shared.generateId(),
            sessionId: activeSessionId,
            questionId: question.dbId,
            selectedIndices: Array(selectedIndices),
            isCorrect: correct,
            timeSpentSeconds: timeSpent,
            answeredAt: Date()
        ))
    }

    func nextQuestion() {
        let nextIdx = currentIndex + 1
        if nextIdx >= questions.count {
            finishQuiz()
        } else {
            currentIndex = nextIdx
            currentQuestion = questions[nextIdx]
            selectedIndices = []
            isAnswerChecked = false
            questionStartTime = Date()
        }
    }

    func skipQuestion() {
        guard let question = currentQuestion else { return }
        let timeSpent = Int(Date().timeIntervalSince(questionStartTime))
        collectedAnswers.append(UserAnswer(
            id: PersistenceController.shared.generateId(),
            sessionId: activeSessionId,
            questionId: question.dbId,
            selectedIndices: [],
            isCorrect: false,
            timeSpentSeconds: timeSpent,
            answeredAt: Date()
        ))
        nextQuestion()
    }

    func toggleBookmark() {
        guard let question = currentQuestion else { return }
        Task {
            await repository.setBookmark(id: question.dbId, bookmarked: !question.isBookmarked)
            if let idx = questions.firstIndex(where: { $0.dbId == question.dbId }) {
                var updated = questions[idx]
                updated.isBookmarked.toggle()
                questions[idx] = updated
                currentQuestion = questions[currentIndex]
            }
        }
    }

    // MARK: - Private

    private func finishQuiz() {
        timerTask?.cancel()
        Task {
            await repository.saveAnswers(collectedAnswers)
            let correctCount = collectedAnswers.filter { $0.isCorrect }.count
            let totalDuration = collectedAnswers.reduce(0) { $0 + $1.timeSpentSeconds }
            let session = Session(
                id: activeSessionId,
                sessionType: params.sessionType,
                fachrichtung: params.fachrichtung,
                examType: params.examType,
                lernfeld: params.lernfeld == -1 ? nil : params.lernfeld,
                totalQuestions: questions.count,
                correctAnswers: correctCount,
                durationSeconds: totalDuration,
                completedAt: Date(),
                startedAt: Date(),
                questionIds: []
            )
            await repository.finishSession(session)
            quizFinished = true
        }
    }

    private func startTimer() {
        timerTask = Task {
            while timerSeconds > 0 {
                try? await Task.sleep(nanoseconds: 1_000_000_000)
                if Task.isCancelled { return }
                timerSeconds -= 1
            }
            finishQuiz()
        }
    }

    func cancelTimer() {
        timerTask?.cancel()
    }
}
