import SwiftUI

struct LernfeldDetailView: View {
    @EnvironmentObject var router: AppRouter
    @EnvironmentObject var appState: AppState
    let item: LernfeldUiItem

    private var lf: Lernfeld { item.lernfeld }
    private var accentColor: Color { .forFachrichtung(item.fachrichtung) }

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {

                // MARK: Header Card
                VStack(spacing: 12) {
                    ZStack {
                        Circle().fill(accentColor.opacity(0.15))
                        Text("LF\(lf.number)")
                            .font(.title.bold())
                            .foregroundColor(accentColor)
                    }
                    .frame(width: 80, height: 80)

                    Text(lf.title)
                        .font(.headline)
                        .multilineTextAlignment(.center)

                    HStack(spacing: 16) {
                        Label("\(lf.stunden) Std.", systemImage: "clock")
                        Label(item.fachrichtung.displayName, systemImage: "person.fill")
                    }
                    .font(.caption)
                    .foregroundColor(.secondary)
                }
                .padding(20)
                .frame(maxWidth: .infinity)
                .background(accentColor.opacity(0.08))
                .clipShape(RoundedRectangle(cornerRadius: 16))
                .padding(.horizontal)

                // MARK: Description
                VStack(alignment: .leading, spacing: 8) {
                    Text("Themen")
                        .font(.subheadline.bold())
                    Text(lf.description)
                        .font(.body)
                        .foregroundColor(.secondary)
                }
                .padding(16)
                .frame(maxWidth: .infinity, alignment: .leading)
                .efCard()
                .padding(.horizontal)

                // MARK: Start button
                Button {
                    router.startQuiz(
                        sessionType:  .LERNFELD,
                        lernfeld:     lf.number,
                        fachrichtung: item.fachrichtung,
                        examType:     .AP1
                    )
                } label: {
                    Text("Training starten")
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 16)
                        .background(accentColor)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                }
                .padding(.horizontal)
                .padding(.top, 8)

                Spacer(minLength: 30)
            }
            .padding(.top)
        }
        .navigationTitle("Lernfeld \(lf.number)")
        .navigationBarTitleDisplayMode(.inline)
    }
}
