import SwiftUI

struct MainTabView: View {
    @EnvironmentObject var router:   AppRouter
    @EnvironmentObject var appState: AppState

    var body: some View {
        TabView {
            HomeView()
                .tabItem { Label("Home",         systemImage: "house.fill") }

            LernfelderView()
                .tabItem { Label("Lernfelder",   systemImage: "list.bullet") }

            ExamView()
                .tabItem { Label("Pr\u{00fc}fung", systemImage: "doc.text.fill") }

            StatsView()
                .tabItem { Label("Statistiken",  systemImage: "chart.bar.fill") }

            SettingsView()
                .tabItem { Label("Einstellungen", systemImage: "gear") }
        }
        .tint(.brandPrimary)
        // Quiz full-screen modal (hides tab bar)
        .fullScreenCover(item: $router.quizParams) { params in
            NavigationStack {
                QuizView(params: params)
            }
        }
        // Result full-screen modal
        .fullScreenCover(item: $router.resultParams) { params in
            NavigationStack {
                ResultView(params: params)
            }
        }
    }
}
