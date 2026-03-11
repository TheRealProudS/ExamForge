import Foundation

@MainActor
final class ResultViewModel: ObservableObject {
    @Published var quizResult: QuizResult?

    private let repository: QuestionRepository

    init(repository: QuestionRepository) {
        self.repository = repository
    }

    func loadResult(sessionId: Int64) {
        Task {
            guard let session = await repository.getSessionById(id: sessionId) else { return }
            let answers = await repository.getAnswersForSession(sessionId: sessionId)

            var questionResults: [QuestionResult] = []
            for answer in answers {
                let question = await repository.getQuestionById(id: answer.questionId)
                questionResults.append(QuestionResult(
                    questionId:      answer.questionId,
                    questionText:    question?.questionText ?? "",
                    options:         question?.options ?? [],
                    correctIndices:  question?.correctAnswerIndices ?? [],
                    selectedIndices: answer.selectedIndices,
                    isCorrect:       answer.isCorrect,
                    explanation:     question?.explanation ?? ""
                ))
            }

            let total       = questionResults.count
            let correct     = questionResults.filter { $0.isCorrect }.count
            let scorePercent = total > 0 ? (correct * 100 / total) : 0
            let skipped     = questionResults.filter { $0.selectedIndices.isEmpty }.count

            quizResult = QuizResult(
                sessionId:       sessionId,
                totalQuestions:  total,
                correctAnswers:  correct,
                wrongAnswers:    total - correct - skipped,
                skippedAnswers:  skipped,
                scorePercent:    scorePercent,
                passed:          scorePercent >= 50,
                timeSeconds:     session.durationSeconds,
                questionResults: questionResults
            )
        }
    }
}
