import Foundation

@MainActor
final class StatsViewModel: ObservableObject {
    @Published var userStats: UserStats?
    @Published var lernfeldProgress: [LernfeldProgress] = []
    @Published var recentSessions: [Session] = []

    private let repository: QuestionRepository

    init(repository: QuestionRepository) {
        self.repository = repository
    }

    func loadStats() {
        Task {
            userStats         = await repository.getUserStats()
            lernfeldProgress  = await repository.getLernfeldProgress()
            recentSessions    = await repository.getRecentSessions(limit: 10)
        }
    }
}
