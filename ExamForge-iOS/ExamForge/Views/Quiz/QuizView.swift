import SwiftUI

struct QuizView: View {
    @EnvironmentObject var router: AppRouter
    @StateObject private var viewModel: QuizViewModel
    @State private var showExitAlert = false
    let params: QuizParams

    init(params: QuizParams) {
        self.params = params
        _viewModel = StateObject(wrappedValue: QuizViewModel(params: params, repository: QuestionRepository()))
    }

    private var isExamMode: Bool { params.sessionType == .EXAM_SIMULATION }

    var body: some View {
        ZStack {
            if viewModel.isLoading {
                ProgressView("Lade Fragen...")
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if viewModel.questions.isEmpty {
                EmptyQuestionsView { router.quizParams = nil }
            } else {
                quizContent
            }
        }
        .navigationBarHidden(true)
        .onAppear { viewModel.start() }
        .onChange(of: viewModel.quizFinished) { finished in
            if finished {
                router.showResult(sessionId: viewModel.sessionId, originalParams: params)
            }
        }
        .alert("Quiz verlassen?", isPresented: $showExitAlert) {
            Button("Verlassen", role: .destructive) {
                viewModel.cancelTimer()
                router.quizParams = nil
            }
            Button("Weiter", role: .cancel) {}
        } message: {
            Text("Dein Fortschritt in dieser Sitzung geht verloren.")
        }
    }

    // MARK: - Main Quiz Content

    private var quizContent: some View {
        VStack(spacing: 0) {

            // MARK: Header bar
            HStack {
                Button { showExitAlert = true } label: {
                    Image(systemName: "xmark")
                        .font(.title3.weight(.semibold))
                        .foregroundColor(.primary)
                        .padding(8)
                }
                Spacer()
                Text("Frage \(viewModel.currentIndex + 1) von \(viewModel.questions.count)")
                    .font(.subheadline.weight(.semibold))
                Spacer()
                HStack(spacing: 12) {
                    if isExamMode {
                        TimerLabel(seconds: viewModel.timerSeconds)
                    }
                    if let question = viewModel.currentQuestion {
                        Button { viewModel.toggleBookmark() } label: {
                            Image(systemName: question.isBookmarked ? "bookmark.fill" : "bookmark")
                                .foregroundColor(question.isBookmarked ? .brandPrimary : .secondary)
                                .padding(8)
                        }
                    }
                }
            }
            .padding(.horizontal)
            .padding(.top, 8)

            // Progress bar
            ProgressView(value: Double(viewModel.currentIndex + 1), total: Double(viewModel.questions.count))
                .tint(.brandPrimary)
                .padding(.horizontal)

            // MARK: Scrollable question area
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    if let question = viewModel.currentQuestion {

                        // Badges
                        HStack(spacing: 8) {
                            BadgePill(text: "LF \(question.lernfeld)", color: .brandPrimary)
                            BadgePill(text: difficultyLabel(question.difficulty),
                                      color: difficultyColor(question.difficulty))
                            if question.correctAnswerIndices.count > 1 {
                                BadgePill(text: "Mehrfachauswahl", color: .secondary)
                            }
                        }

                        // Question text
                        Text(question.questionText)
                            .font(.body)
                            .lineSpacing(5)
                            .fixedSize(horizontal: false, vertical: true)

                        // Answer options
                        ForEach(Array(question.options.enumerated()), id: \.offset) { idx, option in
                            AnswerOption(
                                label:          optionLabel(idx),
                                text:           option,
                                state:          optionState(idx, question: question),
                                onTap:          { viewModel.toggleOption(idx) },
                                isEnabled:      !viewModel.isAnswerChecked
                            )
                        }

                        // Explanation card (after answer checked, not in exam mode)
                        if viewModel.isAnswerChecked && !isExamMode {
                            ExplanationCard(text: question.explanation)
                        }
                    }
                }
                .padding()
            }

            // MARK: Bottom bar
            Divider()
            HStack(spacing: 12) {
                Button("Überspringen") { viewModel.skipQuestion() }
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .disabled(viewModel.isAnswerChecked)

                Spacer()

                Button {
                    if viewModel.isAnswerChecked {
                        viewModel.nextQuestion()
                    } else {
                        viewModel.checkAnswer()
                    }
                } label: {
                    HStack {
                        Text(viewModel.isAnswerChecked ? "Weiter" : "Antwort prüfen")
                            .font(.headline)
                        Image(systemName: viewModel.isAnswerChecked ? "arrow.right" : "checkmark")
                    }
                    .foregroundColor(.white)
                    .padding(.horizontal, 24).padding(.vertical, 14)
                    .background(Color.brandPrimary)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                }
                .disabled(!viewModel.isAnswerChecked && viewModel.selectedIndices.isEmpty)
            }
            .padding(.horizontal)
            .padding(.vertical, 12)
        }
    }

    // MARK: - Helpers

    private func optionLabel(_ idx: Int) -> String {
        ["A", "B", "C", "D", "E"].indices.contains(idx) ? ["A", "B", "C", "D", "E"][idx] : "\(idx)"
    }

    private func difficultyLabel(_ d: Int) -> String {
        d == 1 ? "Leicht" : d == 2 ? "Mittel" : "Schwer"
    }
    private func difficultyColor(_ d: Int) -> Color {
        d == 1 ? .difficultyEasy : d == 2 ? .difficultyMedium : .difficultyHard
    }

    private func optionState(_ idx: Int, question: Question) -> AnswerOptionState {
        let selected = viewModel.selectedIndices.contains(idx)
        let correct  = question.correctAnswerIndices.contains(idx)
        if !viewModel.isAnswerChecked {
            return selected ? .selected : .normal
        }
        if correct   { return .correct }
        if selected  { return .wrong }
        return .normal
    }
}

// MARK: - Answer Option State

enum AnswerOptionState { case normal, selected, correct, wrong }

// MARK: - AnswerOption View

private struct AnswerOption: View {
    let label: String
    let text: String
    let state: AnswerOptionState
    let onTap: () -> Void
    let isEnabled: Bool

    private var bgColor: Color {
        switch state {
        case .normal:   return .clear
        case .selected: return Color.brandPrimary.opacity(0.08)
        case .correct:  return .colorCorrectBg
        case .wrong:    return .colorWrongBg
        }
    }
    private var strokeColor: Color {
        switch state {
        case .normal:   return Color(.systemGray4)
        case .selected: return .brandPrimary
        case .correct:  return .colorCorrect
        case .wrong:    return .colorWrong
        }
    }
    private var textColor: Color {
        switch state {
        case .normal:   return .primary
        case .selected: return .brandPrimary
        case .correct:  return .colorCorrect
        case .wrong:    return .colorWrong
        }
    }

    var body: some View {
        Button(action: onTap) {
            HStack(alignment: .top, spacing: 10) {
                Text(label)
                    .font(.subheadline.bold())
                    .foregroundColor(textColor)
                    .frame(width: 24)
                Text(text)
                    .font(.subheadline)
                    .foregroundColor(textColor)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                Spacer()
            }
            .padding(14)
            .background(bgColor)
            .overlay(RoundedRectangle(cornerRadius: 10).stroke(strokeColor, lineWidth: 1.5))
            .clipShape(RoundedRectangle(cornerRadius: 10))
        }
        .disabled(!isEnabled)
    }
}

// MARK: - Supporting Views

private struct TimerLabel: View {
    let seconds: Int
    private var isWarning: Bool { seconds <= 600 }
    private var text: String {
        String(format: "%02d:%02d", seconds / 60, seconds % 60)
    }
    var body: some View {
        Text(text)
            .font(.subheadline.monospacedDigit().weight(.semibold))
            .foregroundColor(isWarning ? .colorWrong : .primary)
            .padding(.horizontal, 8).padding(.vertical, 4)
            .background(isWarning ? Color.colorWrongBg : Color(.systemGray5))
            .clipShape(Capsule())
    }
}

private struct BadgePill: View {
    let text: String
    let color: Color
    var body: some View {
        Text(text)
            .font(.caption.bold())
            .foregroundColor(color)
            .padding(.horizontal, 8).padding(.vertical, 3)
            .overlay(Capsule().stroke(color, lineWidth: 1))
            .clipShape(Capsule())
    }
}

private struct ExplanationCard: View {
    let text: String
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Label("Erkl\u{00e4}rung", systemImage: "lightbulb.fill")
                .font(.subheadline.bold())
                .foregroundColor(.brandPrimary)
            Text(text.isEmpty ? "Keine Erkl\u{00e4}rung verf\u{00fc}gbar." : text)
                .font(.subheadline)
                .foregroundColor(.secondary)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.brandPrimary.opacity(0.07))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}

private struct EmptyQuestionsView: View {
    let onDismiss: () -> Void
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "tray.fill")
                .font(.system(size: 60))
                .foregroundColor(.secondary)
            Text("Keine Fragen verf\u{00fc}gbar")
                .font(.headline)
            Text("F\u{00fc}r diese Auswahl gibt es aktuell keine Fragen.")
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            Button("Zur\u{00fc}ck") { onDismiss() }
                .buttonStyle(.borderedProminent)
                .tint(.brandPrimary)
        }
        .padding(32)
    }
}
