import Foundation

@MainActor
final class SettingsViewModel: ObservableObject {
    @Published var fachrichtung: Fachrichtung = .SI
    @Published var isDarkMode: Bool = false
    @Published var isSoundEnabled: Bool = true
    @Published var resetDone: Bool = false

    private let repository: QuestionRepository
    let preferences: AppPreferences

    init(repository: QuestionRepository, preferences: AppPreferences) {
        self.repository  = repository
        self.preferences = preferences
    }

    func loadSettings() {
        fachrichtung  = preferences.fachrichtung
        isDarkMode    = preferences.isDarkMode
        isSoundEnabled = preferences.isSoundEnabled
    }

    func setFachrichtung(_ value: Fachrichtung) {
        fachrichtung = value
        preferences.fachrichtung = value
    }

    func setDarkMode(_ enabled: Bool) {
        isDarkMode = enabled
        preferences.isDarkMode = enabled
    }

    func setSoundEnabled(_ enabled: Bool) {
        isSoundEnabled = enabled
        preferences.isSoundEnabled = enabled
    }

    func resetProgress() {
        Task {
            await repository.resetAllProgress()
            resetDone = true
        }
    }
}
