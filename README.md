# ExamForge

> Eine Android‑App zum Erstellen, Verwalten und Durchführen von Prüfungsfragen und Lernkarten.

## Kurzbeschreibung

`ExamForge` ist eine native Android‑Anwendung (Kotlin) zum Erstellen und Üben von Multiple‑Choice‑ und Freitext‑Fragen. Die App nutzt moderne Android‑Bibliotheken (Jetpack Navigation, Room, ViewModel, LiveData, DataBinding) und richtet sich an Lernende und Lehrende, die Prüfungen und Übungssets verwalten möchten.

## Hauptfunktionen
- Erstellen, Bearbeiten und Löschen von Fragen und Fragebögen
- Durchführung von Quizzen mit Auswertung und Statistiken
- Lokale Persistenz via `Room`
- Einstellbare Quizmodi und Fortschrittsverfolgung
- Animationen mit `Lottie` und Diagramme mit `MPAndroidChart`

## Projektstruktur (kurz)
- `app/` — Android‑App Modul (Kotlin)
  - `src/main/java/de/sysitprep/app` — Quellcode (UI, ViewModels, Repository, DB)
  - `build.gradle.kts` — Modul‑Buildskript
- `LICENSE` — Projektlizenz (MIT)

## Technische Details
- Programmiersprache: Kotlin
- Min SDK: 26
- Target/Compile SDK: 34
- JVM Target: 17
- Wichtige Abhängigkeiten: `androidx.navigation`, `lifecycle`, `room`, `datastore`, `gson`, `lottie`, `MPAndroidChart`

## Voraussetzungen
- Android Studio (Electric Eel oder neuer empfohlen)
- JDK 17
- Gradle Wrapper (im Repo enthalten)

## Lokaler Build & Ausführung

1. Projekt klonen oder in Android Studio öffnen.
2. Sicherstellen, dass der `Gradle Wrapper` verwendet wird.
3. In Android Studio: `Run` → App auf einem Emulator oder Gerät deployen.

Alternativ von der Kommandozeile:

```bash
cd ExamForge
./gradlew clean assembleDebug
```

Auf Windows (Powershell / CMD):

```powershell
.\gradlew.bat clean assembleDebug
```

Die App‑ID ist `de.sysitprep.app` (siehe `app/build.gradle.kts`).

## Tests
- Unit‑Tests: `./gradlew test`
- Instrumentation: `./gradlew connectedAndroidTest`

## Mitwirken (Contributing)
- Issues anlegen für Fehler oder Feature‑Wünsche.
- PRs sollten eine kurze Beschreibung und (falls nötig) Screenshots/Animierten GIF enthalten.
- Code‑Style: Kotlin idiomatisch, kurze Commits, aussagekräftige Commit‑Messages.

## Lizenz
Dieses Projekt steht unter der MIT‑Lizenz — siehe `LICENSE`.

## Kontakt
Bei Fragen oder Vorschlägen bitte ein Issue öffnen oder den Repo‑Maintainer kontaktieren.
