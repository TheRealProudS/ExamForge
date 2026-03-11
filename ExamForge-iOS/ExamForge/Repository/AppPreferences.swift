import Foundation
import Combine

// MARK: - AppPreferences

final class AppPreferences: ObservableObject {
    private let defaults = UserDefaults.standard

    @Published var fachrichtung: Fachrichtung {
        didSet { defaults.set(fachrichtung.rawValue, forKey: Keys.fachrichtung) }
    }
    @Published var isDarkMode: Bool {
        didSet { defaults.set(isDarkMode, forKey: Keys.darkMode) }
    }
    @Published var isSoundEnabled: Bool {
        didSet { defaults.set(isSoundEnabled, forKey: Keys.soundEnabled) }
    }

    var seedVersion: String? {
        get { defaults.string(forKey: Keys.seedVersion) }
        set { defaults.set(newValue, forKey: Keys.seedVersion) }
    }

    init() {
        let fachStr = UserDefaults.standard.string(forKey: Keys.fachrichtung) ?? Fachrichtung.SI.rawValue
        self.fachrichtung   = Fachrichtung(rawValue: fachStr) ?? .SI
        self.isDarkMode     = UserDefaults.standard.bool(forKey: Keys.darkMode)
        self.isSoundEnabled = UserDefaults.standard.object(forKey: Keys.soundEnabled) as? Bool ?? true
    }

    private enum Keys {
        static let fachrichtung = "fachrichtung"
        static let darkMode     = "dark_mode"
        static let soundEnabled = "sound_enabled"
        static let seedVersion  = "seed_version"
    }
}
