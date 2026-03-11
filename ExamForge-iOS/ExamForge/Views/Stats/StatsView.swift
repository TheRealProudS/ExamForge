import SwiftUI

struct StatsView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var viewModel: StatsViewModel

    init() {
        _viewModel = StateObject(wrappedValue: StatsViewModel(repository: QuestionRepository()))
    }

    private var hasData: Bool { (viewModel.userStats?.totalAnswered ?? 0) > 0 }

    var body: some View {
        NavigationStack {
            Group {
                if !hasData {
                    emptyState
                } else {
                    statsContent
                }
            }
            .navigationTitle("Statistiken")
        }
        .onAppear { viewModel.loadStats() }
    }

    // MARK: - Empty State

    private var emptyState: some View {
        VStack(spacing: 16) {
            Image(systemName: "chart.bar.xaxis")
                .font(.system(size: 64))
                .foregroundColor(.secondary)
            Text("Noch keine Statistiken")
                .font(.headline)
            Text("Starte ein Quiz, um deine Statistiken zu sehen.")
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
        }
        .padding(32)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    // MARK: - Stats Content

    private var statsContent: some View {
        ScrollView {
            VStack(spacing: 16) {

                // MARK: 4 stat cards
                if let stats = viewModel.userStats {
                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                        StatCard(title: "Beantwortet", value: "\(stats.totalAnswered)", icon: "questionmark.circle.fill", color: .brandPrimary)
                        StatCard(title: "Genauigkeit", value: "\(stats.accuracyPercent)%", icon: "target", color: .colorCorrect)
                        StatCard(title: "Lernzeit", value: formatStudyTime(stats.totalStudySeconds), icon: "clock.fill", color: .brandSecondary)
                        StatCard(title: "Sitzungen", value: "\(stats.completedSessions)", icon: "checkmark.seal.fill", color: .lfAE)
                    }
                    .padding(.horizontal)
                }

                // MARK: Lernfeld progress
                if !viewModel.lernfeldProgress.isEmpty {
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Lernfeld Fortschritt")
                            .font(.headline)
                            .padding(.horizontal)
                        ForEach(viewModel.lernfeldProgress) { lp in
                            LernfeldProgressRow(item: lp)
                                .padding(.horizontal)
                        }
                    }
                }

                // MARK: Recent sessions
                if !viewModel.recentSessions.isEmpty {
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Letzte Sitzungen")
                            .font(.headline)
                            .padding(.horizontal)
                        ForEach(viewModel.recentSessions) { session in
                            SessionRow(session: session)
                                .padding(.horizontal)
                        }
                    }
                }

                Spacer(minLength: 20)
            }
            .padding(.top)
        }
    }

    private func formatStudyTime(_ seconds: Int) -> String {
        let hours = seconds / 3600
        let mins  = (seconds % 3600) / 60
        return hours > 0 ? "\(hours)h \(mins)m" : "\(mins)m"
    }
}

// MARK: - Sub-components

private struct StatCard: View {
    let title: String
    let value: String
    let icon: String
    let color: Color
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Image(systemName: icon).font(.title2).foregroundColor(color)
            Text(value).font(.title2.bold())
            Text(title).font(.caption).foregroundColor(.secondary)
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .efCard()
    }
}

private struct LernfeldProgressRow: View {
    let item: LernfeldProgress
    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack {
                Text("LF \(item.lernfeld)").font(.caption.bold()).foregroundColor(.brandPrimary)
                Text(item.title).font(.caption).foregroundColor(.secondary).lineLimit(1)
                Spacer()
                Text("\(item.correctAnswers)/\(item.totalQuestions) richtig")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            ProgressView(value: Double(item.progressPercent), total: 100)
                .tint(.brandPrimary)
        }
        .padding(12)
        .efCard()
    }
}

private struct SessionRow: View {
    let session: Session

    private var typeLabel: String {
        switch session.sessionType {
        case .LERNFELD:         return "LF \(session.lernfeld ?? 0) Training"
        case .EXAM_SIMULATION:  return "Pr\u{00fc}fungssimulation"
        case .QUICK_QUIZ:       return "Schnell-Quiz"
        case .BOOKMARK_REVIEW:  return "Lesezeichen"
        }
    }

    private var dateString: String {
        let fmt = DateFormatter()
        fmt.dateFormat = "dd.MM.yyyy HH:mm"
        fmt.locale = Locale(identifier: "de_DE")
        return fmt.string(from: session.startedAt)
    }

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text(typeLabel).font(.subheadline.bold())
                Text(dateString).font(.caption).foregroundColor(.secondary)
            }
            Spacer()
            VStack(alignment: .trailing, spacing: 4) {
                let total = session.totalQuestions
                let correct = session.correctAnswers
                let pct = total > 0 ? correct * 100 / total : 0
                Text("\(correct)/\(total) (\(pct)%)").font(.subheadline.bold())
                let d = session.durationSeconds
                Text(d >= 60 ? "\(d/60)m \(d%60)s" : "\(d)s").font(.caption).foregroundColor(.secondary)
            }
        }
        .padding(12)
        .efCard()
    }
}
