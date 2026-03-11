import SwiftUI

struct ResultView: View {
    @EnvironmentObject var router: AppRouter
    @StateObject private var viewModel: ResultViewModel
    let params: ResultParams

    init(params: ResultParams) {
        self.params = params
        _viewModel = StateObject(wrappedValue: ResultViewModel(repository: QuestionRepository()))
    }

    private var result: QuizResult? { viewModel.quizResult }
    private var isPassed: Bool { result?.passed ?? false }
    private var scoreColor: Color { isPassed ? .colorCorrect : .colorWrong }

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {

                // MARK: Score circle
                ZStack {
                    Circle()
                        .stroke(scoreColor.opacity(0.2), lineWidth: 10)
                    Circle()
                        .trim(from: 0, to: CGFloat(result?.scorePercent ?? 0) / 100)
                        .stroke(scoreColor, style: StrokeStyle(lineWidth: 10, lineCap: .round))
                        .rotationEffect(.degrees(-90))
                    VStack(spacing: 4) {
                        Text("\(result?.scorePercent ?? 0)%")
                            .font(.system(size: 40, weight: .bold, design: .rounded))
                            .foregroundColor(scoreColor)
                        Text(isPassed ? "Bestanden! \u{1f389}" : "Nicht bestanden")
                            .font(.subheadline.bold())
                            .foregroundColor(scoreColor)
                    }
                }
                .frame(width: 160, height: 160)
                .animation(.easeOut(duration: 1), value: result?.scorePercent)
                .padding(.top, 24)

                // MARK: Stats Row
                if let r = result {
                    HStack(spacing: 0) {
                        ResultStat(icon: "checkmark.circle.fill", value: "\(r.correctAnswers)",
                                   label: "Richtig", color: .colorCorrect)
                        Divider().frame(height: 50)
                        ResultStat(icon: "xmark.circle.fill", value: "\(r.wrongAnswers)",
                                   label: "Falsch", color: .colorWrong)
                        Divider().frame(height: 50)
                        ResultStat(icon: "clock.fill",
                                   value: String(format: "%d:%02d", r.timeSeconds / 60, r.timeSeconds % 60),
                                   label: "Zeit", color: .brandPrimary)
                    }
                    .padding()
                    .efCard()
                    .padding(.horizontal)
                }

                // MARK: Buttons
                VStack(spacing: 12) {
                    Button {
                        router.retryQuiz()
                    } label: {
                        Text("Nochmal versuchen")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 16)
                            .background(Color.brandPrimary)
                            .clipShape(RoundedRectangle(cornerRadius: 14))
                    }

                    Button {
                        router.dismissResult()
                    } label: {
                        Text("Zum Hauptmen\u{00fc}")
                            .font(.headline)
                            .foregroundColor(.brandPrimary)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 16)
                            .overlay(RoundedRectangle(cornerRadius: 14).stroke(Color.brandPrimary, lineWidth: 1.5))
                    }
                }
                .padding(.horizontal)
                .padding(.bottom, 24)
            }
        }
        .navigationTitle("Ergebnis")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .onAppear { viewModel.loadResult(sessionId: params.sessionId) }
    }
}

// MARK: - ResultStat

private struct ResultStat: View {
    let icon: String
    let value: String
    let label: String
    let color: Color
    var body: some View {
        VStack(spacing: 6) {
            Image(systemName: icon).foregroundColor(color).font(.title3)
            Text(value).font(.title3.bold())
            Text(label).font(.caption).foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
    }
}
