import SwiftUI

struct LernfelderView: View {
    @EnvironmentObject var router: AppRouter
    @EnvironmentObject var appState: AppState
    @StateObject private var viewModel: LernfelderViewModel

    init() {
        _viewModel = StateObject(wrappedValue: LernfelderViewModel(repository: QuestionRepository()))
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {

                // MARK: Chip filter
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ChipButton(title: "Gemeinsam", isSelected: viewModel.selectedFachrichtung == .GEMEINSAM) {
                            viewModel.loadLernfelder(.GEMEINSAM)
                        }
                        ChipButton(title: "Systemintegration", isSelected: viewModel.selectedFachrichtung == .SI) {
                            viewModel.loadLernfelder(.SI)
                        }
                        ChipButton(title: "Anwendungsentwicklung", isSelected: viewModel.selectedFachrichtung == .AE) {
                            viewModel.loadLernfelder(.AE)
                        }
                    }
                    .padding(.horizontal)
                    .padding(.vertical, 10)
                }

                Divider()

                // MARK: List
                List(viewModel.lernfelder) { item in
                    NavigationLink {
                        LernfeldDetailView(item: item)
                    } label: {
                        LernfeldCardRow(item: item)
                    }
                    .listRowInsets(EdgeInsets(top: 6, leading: 16, bottom: 6, trailing: 16))
                    .listRowSeparator(.hidden)
                }
                .listStyle(.plain)
            }
            .navigationTitle("Lernfelder")
        }
        .onAppear { viewModel.loadLernfelder(viewModel.selectedFachrichtung) }
    }
}

// MARK: - Card Row

private struct LernfeldCardRow: View {
    let item: LernfeldUiItem
    private var lf: Lernfeld { item.lernfeld }
    private var accentColor: Color { .forFachrichtung(item.fachrichtung) }
    private var badge: String {
        if lf.fachrichtungen.contains(.GEMEINSAM) { return "Gemeinsam" }
        if lf.fachrichtungen.contains(.SI)        { return "SI" }
        return "AE"
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(alignment: .top) {
                // LF badge circle
                ZStack {
                    Circle().fill(accentColor)
                    Text("LF\(lf.number)")
                        .font(.caption.bold())
                        .foregroundColor(.white)
                }
                .frame(width: 44, height: 44)

                VStack(alignment: .leading, spacing: 4) {
                    HStack(spacing: 6) {
                        Text(badge)
                            .font(.caption2.bold())
                            .foregroundColor(.white)
                            .padding(.horizontal, 6).padding(.vertical, 2)
                            .background(accentColor)
                            .clipShape(Capsule())
                        Text("\(item.questionCount) Fragen")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    Text(lf.title)
                        .font(.subheadline.bold())
                        .lineLimit(2)
                    Text(lf.description)
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .lineLimit(2)
                }
                .padding(.leading, 6)
            }

            // Progress bar
            VStack(alignment: .leading, spacing: 4) {
                ProgressView(value: Double(item.progress), total: 100)
                    .tint(accentColor)
                Text("\(item.progress)%")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        }
        .padding(14)
        .efCard()
    }
}

// MARK: - Chip Button

struct ChipButton: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.subheadline.weight(isSelected ? .semibold : .regular))
                .padding(.horizontal, 14).padding(.vertical, 8)
                .background(isSelected ? Color.brandPrimary : Color(.systemGray5))
                .foregroundColor(isSelected ? .white : .primary)
                .clipShape(Capsule())
        }
    }
}
