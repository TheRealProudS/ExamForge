import SwiftUI

struct ExamView: View {
    @EnvironmentObject var router: AppRouter
    @EnvironmentObject var appState: AppState
    @State private var pendingExam: (examType: ExamType, fachrichtung: Fachrichtung)?
    @State private var showConfirmation = false

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 16) {

                    // MARK: Header
                    HStack {
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Pr\u{00fc}fungssimulation")
                                .font(.title2.bold())
                            Text("W\u{00e4}hle deine Pr\u{00fc}fung")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                        Spacer()
                        Image(systemName: "doc.text.magnifyingglass")
                            .font(.system(size: 36))
                            .foregroundColor(.brandPrimary)
                    }
                    .padding(20)
                    .background(Color.brandPrimary.opacity(0.08))
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                    .padding(.horizontal)

                    // MARK: Exam Cards
                    ExamCard(
                        title: "AP1 – Pr\u{00fc}fung Teil 1",
                        subtitle: "Gemeinsame Pr\u{00fc}fung f\u{00fc}r alle Fachrichtungen",
                        details: "90 Min · 40 Fragen",
                        color: .brandPrimary, icon: "1.circle.fill"
                    ) {
                        pendingExam = (.AP1, .SI)
                        showConfirmation = true
                    }

                    ExamCard(
                        title: "AP2 Systemintegration",
                        subtitle: "Fachinformatiker Systemintegration",
                        details: "90 Min · 40 Fragen",
                        color: .lfSI, icon: "server.rack"
                    ) {
                        pendingExam = (.AP2, .SI)
                        showConfirmation = true
                    }

                    ExamCard(
                        title: "AP2 Anwendungsentwicklung",
                        subtitle: "Fachinformatiker Anwendungsentwicklung",
                        details: "90 Min · 40 Fragen",
                        color: .lfAE, icon: "swift"
                    ) {
                        pendingExam = (.AP2, .AE)
                        showConfirmation = true
                    }

                    // MARK: Info Card
                    VStack(alignment: .leading, spacing: 10) {
                        Label("Pr\u{00fc}fungsbedingungen", systemImage: "info.circle")
                            .font(.subheadline.bold())
                        Text("\u{2022} Zeitlimit: 90 Minuten\n\u{2022} Keine Erkl\u{00e4}rungen w\u{00e4}hrend der Pr\u{00fc}fung\n\u{2022} Bestanden ab 50% richtiger Antworten")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .efCard()
                    .padding(.horizontal)

                    Spacer(minLength: 20)
                }
                .padding(.top)
            }
            .navigationBarHidden(true)
        }
        .alert("Pr\u{00fc}fung starten?", isPresented: $showConfirmation) {
            Button("Jetzt starten") {
                if let exam = pendingExam {
                    router.startQuiz(sessionType: .EXAM_SIMULATION, fachrichtung: exam.fachrichtung, examType: exam.examType)
                }
            }
            Button("Abbrechen", role: .cancel) { pendingExam = nil }
        } message: {
            if let exam = pendingExam {
                Text("Du startest jetzt die Pr\u{00fc}fungssimulation (\(exam.examType.displayName)). Du hast 90 Minuten Zeit.")
            }
        }
    }
}

// MARK: - ExamCard

private struct ExamCard: View {
    let title: String
    let subtitle: String
    let details: String
    let color: Color
    let icon: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 16) {
                ZStack {
                    RoundedRectangle(cornerRadius: 12).fill(color.opacity(0.15))
                    Image(systemName: icon)
                        .font(.title2)
                        .foregroundColor(color)
                }
                .frame(width: 56, height: 56)

                VStack(alignment: .leading, spacing: 4) {
                    Text(title).font(.headline).foregroundColor(.primary)
                    Text(subtitle).font(.caption).foregroundColor(.secondary)
                    Text(details)
                        .font(.caption.bold())
                        .foregroundColor(color)
                }
                Spacer()
                Image(systemName: "chevron.right")
                    .foregroundColor(.secondary)
            }
            .padding(16)
            .efCard()
        }
        .padding(.horizontal)
    }
}
