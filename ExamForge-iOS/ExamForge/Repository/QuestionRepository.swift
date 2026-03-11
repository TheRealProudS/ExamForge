import Foundation
import CoreData

// MARK: - QuestionRepository

final class QuestionRepository {
    private let persistence = PersistenceController.shared
    let preferences = AppPreferences()

    private let SEED_VERSION = "4"

    // MARK: - Seeding

    func seedIfEmpty() async {
        let context = persistence.newBackgroundContext()
        await context.perform {
            let count = self.countAll(in: context)
            let storedVersion = self.preferences.seedVersion
            if count == 0 || storedVersion != self.SEED_VERSION {
                if count > 0 { self.deleteAllQuestions(in: context) }
                let questions = self.loadQuestionsFromBundle()
                for q in questions { self.insertQuestionEntity(q, in: context) }
                self.persistence.save(context: context)
                DispatchQueue.main.async { self.preferences.seedVersion = self.SEED_VERSION }
            }
        }
    }

    private func loadQuestionsFromBundle() -> [Question] {
        var result: [Question] = []
        let fileNames = ["lf_gemeinsam_ap1", "lf_si_ap2", "lf_ae_ap2"]
        let decoder = JSONDecoder()
        for name in fileNames {
            // Try with subdirectory first, then flat bundle lookup
            let url = Bundle.main.url(forResource: name, withExtension: "json", subdirectory: "questions")
                   ?? Bundle.main.url(forResource: name, withExtension: "json")
            guard
                let resolvedURL = url,
                let data = try? Data(contentsOf: resolvedURL),
                let items = try? decoder.decode([QuestionJSON].self, from: data)
            else { continue }
            result += items.compactMap { $0.toQuestion() }
        }
        return result
    }

    // MARK: - Question Queries

    func getQuestionsForLernfeld(lernfeld: Int, fachrichtung: Fachrichtung, limit: Int = 20) async -> [Question] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(
                format: "lernfeld == %d AND (fachrichtung == %@ OR fachrichtung == %@)",
                lernfeld, fachrichtung.rawValue, Fachrichtung.GEMEINSAM.rawValue
            )
            let all = (try? context.fetch(req)) ?? []
            return all.shuffled().prefix(limit).compactMap { self.mapToQuestion($0) }
        }
    }

    func getQuestionsForExam(examType: ExamType, fachrichtung: Fachrichtung, limit: Int = 40) async -> [Question] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(
                format: "examType == %@ AND (fachrichtung == %@ OR fachrichtung == %@)",
                examType.rawValue, fachrichtung.rawValue, Fachrichtung.GEMEINSAM.rawValue
            )
            let all = (try? context.fetch(req)) ?? []
            return all.shuffled().prefix(limit).compactMap { self.mapToQuestion($0) }
        }
    }

    func getRandomQuestions(fachrichtung: Fachrichtung, limit: Int = 10) async -> [Question] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(
                format: "fachrichtung == %@ OR fachrichtung == %@",
                fachrichtung.rawValue, Fachrichtung.GEMEINSAM.rawValue
            )
            let all = (try? context.fetch(req)) ?? []
            return all.shuffled().prefix(limit).compactMap { self.mapToQuestion($0) }
        }
    }

    func getBookmarkedQuestions() async -> [Question] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(format: "isBookmarked == YES")
            req.sortDescriptors = [NSSortDescriptor(key: "createdAt", ascending: false)]
            return ((try? context.fetch(req)) ?? []).compactMap { self.mapToQuestion($0) }
        }
    }

    func setBookmark(id: Int64, bookmarked: Bool) async {
        let context = persistence.newBackgroundContext()
        await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(format: "dbId == %lld", id)
            if let entity = (try? context.fetch(req))?.first {
                entity.setValue(bookmarked, forKey: "isBookmarked")
                self.persistence.save(context: context)
            }
        }
    }

    func getQuestionById(id: Int64) async -> Question? {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(format: "dbId == %lld", id)
            return (try? context.fetch(req))?.first.flatMap { self.mapToQuestion($0) }
        }
    }

    func countQuestionsForLernfeld(lernfeld: Int, fachrichtung: Fachrichtung) async -> Int {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
            req.predicate = NSPredicate(
                format: "lernfeld == %d AND (fachrichtung == %@ OR fachrichtung == %@)",
                lernfeld, fachrichtung.rawValue, Fachrichtung.GEMEINSAM.rawValue
            )
            return (try? context.count(for: req)) ?? 0
        }
    }

    // MARK: - Session Operations

    func startSession(_ session: Session) async -> Int64 {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let entity = NSEntityDescription.insertNewObject(forEntityName: "SessionEntity", into: context)
            entity.setValue(session.id,                         forKey: "dbId")
            entity.setValue(session.sessionType.rawValue,       forKey: "sessionType")
            entity.setValue(session.fachrichtung.rawValue,      forKey: "fachrichtung")
            entity.setValue(session.examType?.rawValue,         forKey: "examTypeStr")
            entity.setValue(session.lernfeld.map { NSNumber(value: Int32($0)) }, forKey: "lernfeldNum")
            entity.setValue(Int32(session.totalQuestions),      forKey: "totalQuestions")
            entity.setValue(Int32(0),                           forKey: "correctAnswers")
            entity.setValue(Int32(0),                           forKey: "durationSeconds")
            entity.setValue(session.startedAt,                  forKey: "startedAt")
            entity.setValue(self.encodeIntArray([]),             forKey: "questionIdsJSON")
            self.persistence.save(context: context)
            return session.id
        }
    }

    func finishSession(_ session: Session) async {
        let context = persistence.newBackgroundContext()
        await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "SessionEntity")
            req.predicate = NSPredicate(format: "dbId == %lld", session.id)
            if let entity = (try? context.fetch(req))?.first {
                entity.setValue(Int32(session.correctAnswers),  forKey: "correctAnswers")
                entity.setValue(Int32(session.durationSeconds), forKey: "durationSeconds")
                entity.setValue(session.completedAt,            forKey: "completedAt")
                self.persistence.save(context: context)
            }
        }
    }

    func saveAnswers(_ answers: [UserAnswer]) async {
        let context = persistence.newBackgroundContext()
        await context.perform {
            for answer in answers {
                let entity = NSEntityDescription.insertNewObject(forEntityName: "UserAnswerEntity", into: context)
                entity.setValue(answer.id,                              forKey: "dbId")
                entity.setValue(answer.sessionId,                       forKey: "sessionId")
                entity.setValue(answer.questionId,                      forKey: "questionId")
                entity.setValue(self.encodeIntArray(answer.selectedIndices), forKey: "selectedIndicesJSON")
                entity.setValue(answer.isCorrect,                       forKey: "isCorrect")
                entity.setValue(Int32(answer.timeSpentSeconds),         forKey: "timeSpentSeconds")
                entity.setValue(answer.answeredAt,                      forKey: "answeredAt")
            }
            self.persistence.save(context: context)
        }
    }

    func getSessionById(id: Int64) async -> Session? {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "SessionEntity")
            req.predicate = NSPredicate(format: "dbId == %lld", id)
            return (try? context.fetch(req))?.first.flatMap { self.mapToSession($0) }
        }
    }

    func getAnswersForSession(sessionId: Int64) async -> [UserAnswer] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "UserAnswerEntity")
            req.predicate = NSPredicate(format: "sessionId == %lld", sessionId)
            return ((try? context.fetch(req)) ?? []).compactMap { self.mapToUserAnswer($0) }
        }
    }

    func getRecentSessions(limit: Int = 20) async -> [Session] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let req = NSFetchRequest<NSManagedObject>(entityName: "SessionEntity")
            req.sortDescriptors = [NSSortDescriptor(key: "startedAt", ascending: false)]
            req.fetchLimit = limit
            return ((try? context.fetch(req)) ?? []).compactMap { self.mapToSession($0) }
        }
    }

    func getUserStats() async -> UserStats {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            let answerReq = NSFetchRequest<NSManagedObject>(entityName: "UserAnswerEntity")
            let allAnswers = (try? context.fetch(answerReq)) ?? []
            let totalAnswered = allAnswers.count
            let correctAnswers = allAnswers.filter { $0.value(forKey: "isCorrect") as? Bool == true }.count

            let completedReq = NSFetchRequest<NSManagedObject>(entityName: "SessionEntity")
            completedReq.predicate = NSPredicate(format: "completedAt != nil")
            let completedSessions = (try? context.count(for: completedReq)) ?? 0

            let sessReq = NSFetchRequest<NSManagedObject>(entityName: "SessionEntity")
            sessReq.predicate = NSPredicate(format: "completedAt != nil")
            let sessions = (try? context.fetch(sessReq)) ?? []
            let totalStudySeconds = sessions.reduce(0) { sum, e in
                sum + Int(e.value(forKey: "durationSeconds") as? Int32 ?? 0)
            }

            return UserStats(
                totalAnswered: totalAnswered,
                correctAnswers: correctAnswers,
                completedSessions: completedSessions,
                totalStudySeconds: totalStudySeconds
            )
        }
    }

    func getLernfeldProgress() async -> [LernfeldProgress] {
        let context = persistence.newBackgroundContext()
        return await context.perform {
            var result: [LernfeldProgress] = []
            for lf in 1...12 {
                let qReq = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
                qReq.predicate = NSPredicate(format: "lernfeld == %d", lf)
                let questions = (try? context.fetch(qReq)) ?? []
                let questionIds = questions.compactMap { $0.value(forKey: "dbId") as? Int64 }
                if questionIds.isEmpty { continue }

                let aReq = NSFetchRequest<NSManagedObject>(entityName: "UserAnswerEntity")
                aReq.predicate = NSPredicate(format: "questionId IN %@", questionIds)
                let answers = (try? context.fetch(aReq)) ?? []
                if answers.isEmpty { continue }

                let correct = answers.filter { $0.value(forKey: "isCorrect") as? Bool == true }.count
                result.append(LernfeldProgress(
                    lernfeld: lf, title: "Lernfeld \(lf)",
                    totalQuestions: answers.count, correctAnswers: correct
                ))
            }
            return result
        }
    }

    func resetAllProgress() async {
        let context = persistence.newBackgroundContext()
        await context.perform {
            for entity in ["SessionEntity", "UserAnswerEntity"] {
                let req = NSFetchRequest<NSFetchRequestResult>(entityName: entity)
                try? context.execute(NSBatchDeleteRequest(fetchRequest: req))
            }
            self.persistence.save(context: context)
        }
    }

    // MARK: - Private helpers

    private func countAll(in context: NSManagedObjectContext) -> Int {
        let req = NSFetchRequest<NSManagedObject>(entityName: "QuestionEntity")
        return (try? context.count(for: req)) ?? 0
    }

    private func deleteAllQuestions(in context: NSManagedObjectContext) {
        let req = NSFetchRequest<NSFetchRequestResult>(entityName: "QuestionEntity")
        try? context.execute(NSBatchDeleteRequest(fetchRequest: req))
    }

    private func insertQuestionEntity(_ q: Question, in ctx: NSManagedObjectContext) {
        let entity = NSEntityDescription.insertNewObject(forEntityName: "QuestionEntity", into: ctx)
        entity.setValue(persistence.generateId(),        forKey: "dbId")
        entity.setValue(q.questionKey,                   forKey: "questionKey")
        entity.setValue(q.title,                         forKey: "title")
        entity.setValue(q.questionText,                  forKey: "questionText")
        entity.setValue(encodeStringArray(q.options),    forKey: "optionsJSON")
        entity.setValue(encodeIntArray(q.correctAnswerIndices), forKey: "correctAnswerIndicesJSON")
        entity.setValue(q.explanation,                   forKey: "explanation")
        entity.setValue(Int32(q.lernfeld),               forKey: "lernfeld")
        entity.setValue(q.fachrichtung.rawValue,         forKey: "fachrichtung")
        entity.setValue(q.examType.rawValue,             forKey: "examType")
        entity.setValue(Int32(q.year),                   forKey: "year")
        entity.setValue(Int32(q.difficulty),             forKey: "difficulty")
        entity.setValue(false,                           forKey: "isBookmarked")
        entity.setValue(Date(),                          forKey: "createdAt")
    }

    private func mapToQuestion(_ entity: NSManagedObject) -> Question? {
        guard
            let questionKey = entity.value(forKey: "questionKey") as? String,
            let title       = entity.value(forKey: "title") as? String,
            let questionText = entity.value(forKey: "questionText") as? String,
            let optJSON     = entity.value(forKey: "optionsJSON") as? String,
            let corrJSON    = entity.value(forKey: "correctAnswerIndicesJSON") as? String,
            let fachStr     = entity.value(forKey: "fachrichtung") as? String,
            let examStr     = entity.value(forKey: "examType") as? String,
            let fachrichtung = Fachrichtung(rawValue: fachStr),
            let examType    = ExamType(rawValue: examStr)
        else { return nil }

        return Question(
            dbId:                  entity.value(forKey: "dbId") as? Int64 ?? 0,
            questionKey:           questionKey,
            title:                 title,
            questionText:          questionText,
            options:               decodeStringArray(optJSON),
            correctAnswerIndices:  decodeIntArray(corrJSON),
            explanation:           entity.value(forKey: "explanation") as? String ?? "",
            lernfeld:              Int(entity.value(forKey: "lernfeld") as? Int32 ?? 0),
            fachrichtung:          fachrichtung,
            examType:              examType,
            year:                  Int(entity.value(forKey: "year") as? Int32 ?? 0),
            difficulty:            Int(entity.value(forKey: "difficulty") as? Int32 ?? 1),
            isBookmarked:          entity.value(forKey: "isBookmarked") as? Bool ?? false,
            createdAt:             entity.value(forKey: "createdAt") as? Date ?? Date()
        )
    }

    private func mapToSession(_ entity: NSManagedObject) -> Session? {
        guard
            let typeStr     = entity.value(forKey: "sessionType") as? String,
            let fachStr     = entity.value(forKey: "fachrichtung") as? String,
            let sessionType = SessionType(rawValue: typeStr),
            let fachrichtung = Fachrichtung(rawValue: fachStr)
        else { return nil }

        let examTypeStr = entity.value(forKey: "examTypeStr") as? String
        let lernfeldNum = entity.value(forKey: "lernfeldNum") as? NSNumber

        return Session(
            id:               entity.value(forKey: "dbId") as? Int64 ?? 0,
            sessionType:      sessionType,
            fachrichtung:     fachrichtung,
            examType:         examTypeStr.flatMap { ExamType(rawValue: $0) },
            lernfeld:         lernfeldNum.map { Int(truncating: $0) },
            totalQuestions:   Int(entity.value(forKey: "totalQuestions") as? Int32 ?? 0),
            correctAnswers:   Int(entity.value(forKey: "correctAnswers") as? Int32 ?? 0),
            durationSeconds:  Int(entity.value(forKey: "durationSeconds") as? Int32 ?? 0),
            completedAt:      entity.value(forKey: "completedAt") as? Date,
            startedAt:        entity.value(forKey: "startedAt") as? Date ?? Date(),
            questionIds:      decodeLongArray(entity.value(forKey: "questionIdsJSON") as? String ?? "[]")
        )
    }

    private func mapToUserAnswer(_ entity: NSManagedObject) -> UserAnswer? {
        UserAnswer(
            id:               entity.value(forKey: "dbId") as? Int64 ?? 0,
            sessionId:        entity.value(forKey: "sessionId") as? Int64 ?? 0,
            questionId:       entity.value(forKey: "questionId") as? Int64 ?? 0,
            selectedIndices:  decodeIntArray(entity.value(forKey: "selectedIndicesJSON") as? String ?? "[]"),
            isCorrect:        entity.value(forKey: "isCorrect") as? Bool ?? false,
            timeSpentSeconds: Int(entity.value(forKey: "timeSpentSeconds") as? Int32 ?? 0),
            answeredAt:       entity.value(forKey: "answeredAt") as? Date ?? Date()
        )
    }

    // MARK: - JSON helpers

    private func encodeIntArray(_ array: [Int]) -> String {
        (try? String(data: JSONEncoder().encode(array), encoding: .utf8)) ?? "[]"
    }
    private func decodeIntArray(_ json: String) -> [Int] {
        (try? JSONDecoder().decode([Int].self, from: Data(json.utf8))) ?? []
    }
    private func encodeStringArray(_ array: [String]) -> String {
        (try? String(data: JSONEncoder().encode(array), encoding: .utf8)) ?? "[]"
    }
    private func decodeStringArray(_ json: String) -> [String] {
        (try? JSONDecoder().decode([String].self, from: Data(json.utf8))) ?? []
    }
    private func decodeLongArray(_ json: String) -> [Int64] {
        (try? JSONDecoder().decode([Int64].self, from: Data(json.utf8))) ?? []
    }
}
