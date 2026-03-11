import Foundation

// MARK: - UserStats

struct UserStats {
    var totalAnswered: Int = 0
    var correctAnswers: Int = 0
    var completedSessions: Int = 0
    var totalStudySeconds: Int = 0
    var streakDays: Int = 0
    var lastStudiedAt: Date = Date(timeIntervalSince1970: 0)

    var accuracy: Float {
        totalAnswered == 0 ? 0.0 : Float(correctAnswers) / Float(totalAnswered)
    }
    var accuracyPercent: Int { Int(accuracy * 100) }
}

// MARK: - LernfeldProgress

struct LernfeldProgress: Identifiable {
    let id = UUID()
    let lernfeld: Int
    var title: String
    let totalQuestions: Int
    let correctAnswers: Int

    init(lernfeld: Int, title: String = "", totalQuestions: Int, correctAnswers: Int) {
        self.lernfeld = lernfeld
        self.title = title.isEmpty ? "Lernfeld \(lernfeld)" : title
        self.totalQuestions = totalQuestions
        self.correctAnswers = correctAnswers
    }

    var progressPercent: Int {
        totalQuestions == 0 ? 0 : Int(Float(correctAnswers) / Float(totalQuestions) * 100)
    }
}

// MARK: - QuizResult

struct QuizResult {
    let sessionId: Int64
    let totalQuestions: Int
    let correctAnswers: Int
    let wrongAnswers: Int
    let skippedAnswers: Int
    let scorePercent: Int
    let passed: Bool
    let timeSeconds: Int
    let questionResults: [QuestionResult]
}

struct QuestionResult: Identifiable {
    let id = UUID()
    let questionId: Int64
    let questionText: String
    let options: [String]
    let correctIndices: [Int]
    let selectedIndices: [Int]
    let isCorrect: Bool
    let explanation: String
}

// MARK: - Navigation Params

struct QuizParams: Identifiable {
    let id = UUID()
    let sessionType: SessionType
    let lernfeld: Int          // -1 means not applicable
    let fachrichtung: Fachrichtung
    let examType: ExamType
}

struct ResultParams: Identifiable {
    let id = UUID()
    let sessionId: Int64
    let originalParams: QuizParams
}

// MARK: - Lernfeld UI Item

struct LernfeldUiItem: Identifiable {
    let id = UUID()
    let lernfeld: Lernfeld
    let questionCount: Int
    let progress: Int
    let fachrichtung: Fachrichtung
}
