import Foundation

// MARK: - SessionType

enum SessionType: String, Codable {
    case LERNFELD
    case EXAM_SIMULATION
    case QUICK_QUIZ
    case BOOKMARK_REVIEW

    var displayName: String {
        switch self {
        case .LERNFELD:         return "Lernfeld Training"
        case .EXAM_SIMULATION:  return "Pr\u{00FC}fungssimulation"
        case .QUICK_QUIZ:       return "Schnell-Quiz"
        case .BOOKMARK_REVIEW:  return "Lesezeichen Wiederholung"
        }
    }
}

// MARK: - Session

struct Session: Identifiable {
    let id: Int64
    let sessionType: SessionType
    let fachrichtung: Fachrichtung
    let examType: ExamType?
    let lernfeld: Int?
    let totalQuestions: Int
    var correctAnswers: Int
    var durationSeconds: Int
    var completedAt: Date?
    let startedAt: Date
    var questionIds: [Int64]
}

// MARK: - UserAnswer

struct UserAnswer: Identifiable {
    let id: Int64
    let sessionId: Int64
    let questionId: Int64
    let selectedIndices: [Int]
    let isCorrect: Bool
    let timeSpentSeconds: Int
    let answeredAt: Date
}
