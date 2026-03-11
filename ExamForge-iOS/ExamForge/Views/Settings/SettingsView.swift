import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var viewModel: SettingsViewModel
    @State private var showResetConfirm = false
    @State private var showResetDone    = false

    init() {
        // viewModel instantiated in onAppear so we can use appState
        _viewModel = StateObject(wrappedValue: SettingsViewModel(
            repository: QuestionRepository(),
            preferences: AppPreferences()
        ))
    }

    var body: some View {
        NavigationStack {
            List {

                // MARK: PROFIL
                Section("Profil") {
                    FachrichtungOption(
                        title: "Systemintegration",
                        subtitle: "Fachinformatiker SI",
                        isSelected: viewModel.fachrichtung == .SI
                    ) { viewModel.setFachrichtung(.SI) }

                    FachrichtungOption(
                        title: "Anwendungsentwicklung",
                        subtitle: "Fachinformatiker AE",
                        isSelected: viewModel.fachrichtung == .AE
                    ) { viewModel.setFachrichtung(.AE) }
                }

                // MARK: DARSTELLUNG
                Section("Darstellung") {
                    Toggle("Dark Mode", isOn: Binding(
                        get: { viewModel.isDarkMode },
                        set: { viewModel.setDarkMode($0) }
                    ))
                    Toggle("Soundeffekte", isOn: Binding(
                        get: { viewModel.isSoundEnabled },
                        set: { viewModel.setSoundEnabled($0) }
                    ))
                }

                // MARK: DATEN
                Section("Daten") {
                    Button(role: .destructive) {
                        showResetConfirm = true
                    } label: {
                        Label("Fortschritt zur\u{00fc}cksetzen", systemImage: "trash")
                    }
                }

                // MARK: RECHTLICHES
                Section("Rechtliches") {
                    Link(destination: URL(string: "https://www.growtracker.de/impressum.html")!) {
                        Label("Impressum", systemImage: "doc.text")
                    }
                }

                // MARK: Version
                Section {
                    HStack {
                        Text("Version")
                        Spacer()
                        Text("1.0.0").foregroundColor(.secondary)
                    }
                }
            }
            .navigationTitle("Einstellungen")
        }
        .onAppear { viewModel.loadSettings() }
        .alert("Fortschritt zur\u{00fc}cksetzen?", isPresented: $showResetConfirm) {
            Button("Zur\u{00fc}cksetzen", role: .destructive) { viewModel.resetProgress() }
            Button("Abbrechen", role: .cancel) {}
        } message: {
            Text("Alle Sitzungen und Antworten werden dauerhaft gel\u{00f6}scht. Lesezeichen bleiben erhalten.")
        }
        .alert("Zur\u{00fcck}gesetzt", isPresented: $showResetDone) {
            Button("OK", role: .cancel) {}
        } message: { Text("Dein Fortschritt wurde zur\u{00fc}ckgesetzt.") }
        .onChange(of: viewModel.resetDone) { done in
            if done { showResetDone = true }
        }
        .preferredColorScheme(viewModel.isDarkMode ? .dark : .light)
    }
}

// MARK: - Fachrichtung Option Row

private struct FachrichtungOption: View {
    let title: String
    let subtitle: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(title).font(.body).foregroundColor(.primary)
                    Text(subtitle).font(.caption).foregroundColor(.secondary)
                }
                Spacer()
                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.brandPrimary)
                } else {
                    Image(systemName: "circle")
                        .foregroundColor(.secondary)
                }
            }
        }
    }
}
