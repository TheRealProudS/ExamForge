import Foundation

@MainActor
final class LernfelderViewModel: ObservableObject {
    @Published var lernfelder: [LernfeldUiItem] = []
    @Published var selectedFachrichtung: Fachrichtung = .GEMEINSAM

    private let repository: QuestionRepository

    init(repository: QuestionRepository) {
        self.repository = repository
    }

    func loadLernfelder(_ fachrichtung: Fachrichtung) {
        selectedFachrichtung = fachrichtung
        Task {
            let lfs = LernfelderData.getLernfelderForFachrichtung(fachrichtung)
            var items: [LernfeldUiItem] = []
            for lf in lfs {
                let count = await repository.countQuestionsForLernfeld(lernfeld: lf.number, fachrichtung: fachrichtung)
                items.append(LernfeldUiItem(
                    lernfeld: lf,
                    questionCount: count,
                    progress: 0,
                    fachrichtung: fachrichtung
                ))
            }
            lernfelder = items
        }
    }

    func refresh() {
        loadLernfelder(selectedFachrichtung)
    }
}
