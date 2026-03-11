import Foundation

// MARK: - Enums

enum Fachrichtung: String, Codable, CaseIterable, Identifiable {
    case SI
    case AE
    case GEMEINSAM

    var id: String { rawValue }

    var displayName: String {
        switch self {
        case .SI:        return "Systemintegration"
        case .AE:        return "Anwendungsentwicklung"
        case .GEMEINSAM: return "Gemeinsam (SI + AE)"
        }
    }

    var color: UIColorHex {
        switch self {
        case .SI:        return "#00695C"
        case .AE:        return "#6A1B9A"
        case .GEMEINSAM: return "#1565C0"
        }
    }
}

typealias UIColorHex = String

enum ExamType: String, Codable, CaseIterable {
    case AP1
    case AP2

    var displayName: String {
        switch self {
        case .AP1: return "AP1 \u{2013} Abschlusspr\u{00FC}fung Teil 1"
        case .AP2: return "AP2 \u{2013} Abschlusspr\u{00FC}fung Teil 2"
        }
    }
}

// MARK: - Domain Model

struct Question: Identifiable {
    let id: UUID
    var dbId: Int64
    let questionKey: String
    let title: String
    let questionText: String
    let options: [String]
    let correctAnswerIndices: [Int]
    let explanation: String
    let lernfeld: Int
    let fachrichtung: Fachrichtung
    let examType: ExamType
    let year: Int
    let difficulty: Int
    var isBookmarked: Bool
    let createdAt: Date

    init(
        dbId: Int64 = 0,
        questionKey: String,
        title: String,
        questionText: String,
        options: [String],
        correctAnswerIndices: [Int],
        explanation: String = "",
        lernfeld: Int,
        fachrichtung: Fachrichtung,
        examType: ExamType,
        year: Int = 0,
        difficulty: Int = 1,
        isBookmarked: Bool = false,
        createdAt: Date = Date()
    ) {
        self.id = UUID()
        self.dbId = dbId
        self.questionKey = questionKey
        self.title = title
        self.questionText = questionText
        self.options = options
        self.correctAnswerIndices = correctAnswerIndices
        self.explanation = explanation
        self.lernfeld = lernfeld
        self.fachrichtung = fachrichtung
        self.examType = examType
        self.year = year
        self.difficulty = difficulty
        self.isBookmarked = isBookmarked
        self.createdAt = createdAt
    }
}

// MARK: - JSON Decoding

struct QuestionJSON: Codable {
    let key: String?
    let title: String?
    let questionText: String?
    let options: [String]?
    let correctAnswerIndices: [Int]?
    let explanation: String?
    let lernfeld: Int
    let fachrichtung: String
    let examType: String
    let year: Int?
    let difficulty: Int?

    func toQuestion() -> Question? {
        guard
            let fach = Fachrichtung(rawValue: fachrichtung.uppercased()),
            let exam = ExamType(rawValue: examType.uppercased())
        else { return nil }

        return Question(
            questionKey: key ?? "",
            title: title ?? "",
            questionText: questionText ?? "",
            options: options ?? [],
            correctAnswerIndices: correctAnswerIndices ?? [],
            explanation: explanation ?? "",
            lernfeld: lernfeld,
            fachrichtung: fach,
            examType: exam,
            year: year ?? 0,
            difficulty: difficulty ?? 1
        )
    }
}
