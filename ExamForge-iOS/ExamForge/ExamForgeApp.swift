import SwiftUI

// MARK: - AppState

@MainActor
final class AppState: ObservableObject {
    @Published var showSplash = true
    let repository: QuestionRepository
    let preferences: AppPreferences

    init() {
        self.repository  = QuestionRepository()
        self.preferences = AppPreferences()
        Task {
            await repository.seedIfEmpty()
        }
    }
}

// MARK: - App Entry Point

@main
struct ExamForgeApp: App {
    @StateObject private var appState = AppState()
    @StateObject private var router   = AppRouter()

    var body: some Scene {
        WindowGroup {
            Group {
                if appState.showSplash {
                    SplashView()
                } else {
                    MainTabView()
                }
            }
            .environmentObject(appState)
            .environmentObject(router)
            .preferredColorScheme(appState.preferences.isDarkMode ? .dark : .light)
        }
    }
}
