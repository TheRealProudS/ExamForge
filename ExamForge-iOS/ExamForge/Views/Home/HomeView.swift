import SwiftUI

struct HomeView: View {
    @EnvironmentObject var router:   AppRouter
    @EnvironmentObject var appState: AppState
    @StateObject private var viewModel: HomeViewModel

    init() {
        // ViewModel is created with the shared repository via AppState init trick
        _viewModel = StateObject(wrappedValue: HomeViewModel(repository: QuestionRepository()))
    }

    private var greeting: String {
        let hour = Calendar.current.component(.hour, from: Date())
        if hour < 12 { return "Guten Morgen! \u{2600}\u{fe0f}" }
        else if hour < 17 { return "Guten Nachmittag! \u{1f31e}" }
        else { return "Guten Abend! \u{1f319}" }
    }

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 16) {

                    // MARK: Header Banner
                    ZStack(alignment: .bottomLeading) {
                        LinearGradient(
                            colors: [Color.brandPrimary, Color.brandSecondary],
                            startPoint: .topLeading, endPoint: .bottomTrailing
                        )
                        VStack(alignment: .leading, spacing: 8) {
                            Image(systemName: "graduationcap.fill")
                                .font(.system(size: 48))
                                .foregroundColor(.white.opacity(0.9))
                            Text(greeting)
                                .font(.title2.bold())
                                .foregroundColor(.white)
                            Text("Bereit f\u{00fc}r die n\u{00e4}chste Lerneinheit?")
                                .font(.subheadline)
                                .foregroundColor(.white.opacity(0.85))
                            Text(appState.preferences.fachrichtung.displayName)
                                .font(.caption.weight(.semibold))
                                .padding(.horizontal, 10).padding(.vertical, 4)
                                .background(.white.opacity(0.25))
                                .foregroundColor(.white)
                                .clipShape(Capsule())
                        }
                        .padding(20)
                    }
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                    .padding(.horizontal)

                    // MARK: Stats Row
                    HStack(spacing: 12) {
                        StatBadge(icon: "\u{1f525}", value: "\(viewModel.stats.streakDays)", label: "Tage")
                        StatBadge(icon: "\u{2705}", value: "\(viewModel.stats.totalAnswered)", label: "Beantwortet")
                        StatBadge(icon: "\u{1f3af}", value: "\(viewModel.stats.accuracyPercent)%", label: "Genauigkeit")
                    }
                    .padding(.horizontal)

                    // MARK: Quick Quiz Card
                    Button {
                        router.startQuiz(
                            sessionType:  .QUICK_QUIZ,
                            fachrichtung: appState.preferences.fachrichtung,
                            examType:     .AP1
                        )
                    } label: {
                        HStack {
                            VStack(alignment: .leading, spacing: 6) {
                                Text("Schnell-Quiz")
                                    .font(.headline).foregroundColor(.white)
                                Text("10 zuf\u{00e4}llige Fragen")
                                    .font(.subheadline).foregroundColor(.white.opacity(0.85))
                            }
                            Spacer()
                            Image(systemName: "bolt.fill")
                                .font(.system(size: 32))
                                .foregroundColor(.white.opacity(0.9))
                        }
                        .padding(20)
                        .background(
                            LinearGradient(colors: [Color.brandPrimary, Color.brandSecondary],
                                           startPoint: .leading, endPoint: .trailing)
                        )
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                    }
                    .padding(.horizontal)

                    // MARK: 2-column Row
                    HStack(spacing: 12) {
                        // Exam Simulation
                        NavigationLink {
                            ExamView()
                        } label: {
                            HomeMiniCard(
                                icon: "doc.text.fill",
                                title: "Pr\u{00fc}fungs-\nsimulation",
                                subtitle: "AP1 & AP2",
                                color: .brandSecondary
                            )
                        }

                        // Bookmarks
                        Button {
                            router.startQuiz(
                                sessionType:  .BOOKMARK_REVIEW,
                                fachrichtung: appState.preferences.fachrichtung,
                                examType:     .AP1
                            )
                        } label: {
                            HomeMiniCard(
                                icon: "bookmark.fill",
                                title: "Lesezeichen",
                                subtitle: "\(viewModel.bookmarkCount) Fragen",
                                color: Color(red: 0.82, green: 0.45, blue: 0.0)
                            )
                        }
                    }
                    .padding(.horizontal)

                    Spacer(minLength: 20)
                }
                .padding(.top)
            }
            .navigationBarHidden(true)
        }
        .onAppear { viewModel.refreshStats() }
    }
}

// MARK: - Sub-components

private struct StatBadge: View {
    let icon: String
    let value: String
    let label: String
    var body: some View {
        VStack(spacing: 4) {
            Text(icon).font(.title2)
            Text(value).font(.title3.bold())
            Text(label).font(.caption).foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 12)
        .efCard()
    }
}

private struct HomeMiniCard: View {
    let icon: String
    let title: String
    let subtitle: String
    let color: Color
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Image(systemName: icon)
                .font(.title2)
                .foregroundColor(color)
            Text(title)
                .font(.subheadline.bold())
                .multilineTextAlignment(.leading)
                .foregroundColor(.primary)
            Text(subtitle)
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .efCard()
    }
}
