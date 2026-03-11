import Foundation
import Combine

@MainActor
final class HomeViewModel: ObservableObject {
    @Published var stats = UserStats()
    @Published var bookmarkCount = 0

    private let repository: QuestionRepository

    init(repository: QuestionRepository) {
        self.repository = repository
    }

    func refreshStats() {
        Task {
            stats = await repository.getUserStats()
            let bookmarks = await repository.getBookmarkedQuestions()
            bookmarkCount = bookmarks.count
        }
    }
}
