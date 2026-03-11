import SwiftUI

// MARK: - App Router (shared navigation state)

@MainActor
final class AppRouter: ObservableObject {
    @Published var quizParams: QuizParams?
    @Published var resultParams: ResultParams?

    func startQuiz(sessionType: SessionType, lernfeld: Int = -1, fachrichtung: Fachrichtung, examType: ExamType) {
        quizParams = QuizParams(sessionType: sessionType, lernfeld: lernfeld, fachrichtung: fachrichtung, examType: examType)
    }

    func showResult(sessionId: Int64, originalParams: QuizParams) {
        quizParams = nil
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) { [weak self] in
            self?.resultParams = ResultParams(sessionId: sessionId, originalParams: originalParams)
        }
    }

    func dismissResult() {
        resultParams = nil
    }

    func retryQuiz() {
        guard let params = resultParams?.originalParams else { return }
        resultParams = nil
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) { [weak self] in
            self?.quizParams = params
        }
    }
}

// MARK: - Color Theme

extension Color {
    static let brandPrimary    = Color(red: 0.082, green: 0.392, blue: 0.753) // #1565C0
    static let brandSecondary  = Color(red: 0.012, green: 0.533, blue: 0.820) // #0288D1
    static let lfGemeinsam     = Color(red: 0.082, green: 0.392, blue: 0.753) // #1565C0
    static let lfSI            = Color(red: 0.000, green: 0.412, blue: 0.361) // #00695C
    static let lfAE            = Color(red: 0.416, green: 0.106, blue: 0.604) // #6A1B9A
    static let colorCorrect    = Color(red: 0.298, green: 0.686, blue: 0.314) // #4CAF50
    static let colorWrong      = Color(red: 0.957, green: 0.263, blue: 0.212) // #F44336
    static let colorCorrectBg  = Color(red: 0.910, green: 0.969, blue: 0.910) // #E8F5E9
    static let colorWrongBg    = Color(red: 1.000, green: 0.898, blue: 0.898) // #FFEBEE
    static let difficultyEasy  = Color(red: 0.298, green: 0.686, blue: 0.314)
    static let difficultyMedium = Color(red: 1.000, green: 0.600, blue: 0.000)
    static let difficultyHard  = Color(red: 0.957, green: 0.263, blue: 0.212)

    static func forFachrichtung(_ f: Fachrichtung) -> Color {
        switch f {
        case .SI:        return .lfSI
        case .AE:        return .lfAE
        case .GEMEINSAM: return .lfGemeinsam
        }
    }
}

// MARK: - Shared Card Modifier

struct EFCard: ViewModifier {
    func body(content: Content) -> some View {
        content
            .background(Color(.systemBackground))
            .cornerRadius(12)
            .shadow(color: .black.opacity(0.07), radius: 4, x: 0, y: 2)
    }
}

extension View {
    func efCard() -> some View { modifier(EFCard()) }
}
