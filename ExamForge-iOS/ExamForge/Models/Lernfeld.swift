import Foundation

// MARK: - Lernfeld

struct Lernfeld: Identifiable {
    let number: Int
    let title: String
    let description: String
    let fachrichtungen: [Fachrichtung]
    let stunden: Int

    var id: String { "\(number)-\(fachrichtungen.map(\.rawValue).joined())" }
}

// MARK: - Static Data

enum LernfelderData {
    static let allLernfelder: [Lernfeld] = [
        Lernfeld(number: 1,
                 title: "Das Unternehmen und die eigene Rolle im Betrieb beschreiben",
                 description: "Unternehmensorganisation, Berufsausbildung, Rechte und Pflichten, Betriebsklima",
                 fachrichtungen: [.GEMEINSAM], stunden: 40),
        Lernfeld(number: 2,
                 title: "Arbeitspl\u{00e4}tze nach Kundenwunsch ausstatten",
                 description: "Hardware, Betriebssysteme, Softwarelizenzen, IT-Systeme ausw\u{00e4}hlen und installieren",
                 fachrichtungen: [.GEMEINSAM], stunden: 80),
        Lernfeld(number: 3,
                 title: "Clients in Netzwerke einbinden",
                 description: "OSI-Modell, TCP/IP, IP-Adressierung, DHCP, DNS, WLAN, grundlegende Netzwerkkonfiguration",
                 fachrichtungen: [.GEMEINSAM], stunden: 80),
        Lernfeld(number: 4,
                 title: "Schutzbedarfsanalyse im eigenen Arbeitsbereich durchf\u{00fc}hren",
                 description: "IT-Sicherheit, Datenschutz (DSGVO), Bedrohungsanalyse, Schutzma\u{00df}nahmen",
                 fachrichtungen: [.GEMEINSAM], stunden: 80),
        Lernfeld(number: 5,
                 title: "Software zur Verwaltung von Daten anpassen",
                 description: "Datenbanken, SQL, Datenmodellierung, ER-Diagramme, Normalisierung",
                 fachrichtungen: [.GEMEINSAM], stunden: 80),
        Lernfeld(number: 6,
                 title: "Serviceanfragen bearbeiten",
                 description: "ITIL, Ticketsystem, First-Level-Support, SLA, Incident-Management",
                 fachrichtungen: [.GEMEINSAM], stunden: 40),
        Lernfeld(number: 7,
                 title: "Cyber-physische Systeme erg\u{00e4}nzen",
                 description: "IoT, Sensoren, Aktoren, Mikrocontroller, Schnittstellen, Protokolle",
                 fachrichtungen: [.GEMEINSAM], stunden: 80),
        // SI-specific
        Lernfeld(number: 8,
                 title: "Netzwerke und Dienste bereitstellen",
                 description: "Serverinfrastruktur, Active Directory, DNS, DHCP-Server, Routing, Firewalls",
                 fachrichtungen: [.SI], stunden: 80),
        Lernfeld(number: 9,
                 title: "Netzwerke und Dienste absichern",
                 description: "VPN, Firewall-Konzepte, Verschl\u{00fc}sselung, PKI, Zertifikate, Penetrationstest",
                 fachrichtungen: [.SI], stunden: 80),
        Lernfeld(number: 10,
                 title: "Clients administrieren und absichern",
                 description: "Gruppenrichtlinien, MDM, Virenschutz, Backup-Strategien, Hardening",
                 fachrichtungen: [.SI], stunden: 80),
        Lernfeld(number: 11,
                 title: "Komplexe IT-Systeme einrichten",
                 description: "Virtualisierung, Cloud-Dienste, Hochverf\u{00fc}gbarkeit, Storage, SAN/NAS",
                 fachrichtungen: [.SI], stunden: 80),
        Lernfeld(number: 12,
                 title: "IT-Systeme in Betrieb nehmen",
                 description: "Projektplanung, Migration, Rollout, Dokumentation, ITIL-Betrieb",
                 fachrichtungen: [.SI], stunden: 40),
        // AE-specific
        Lernfeld(number: 8,
                 title: "Daten system\u{00fc}bergreifend bereitstellen",
                 description: "REST-APIs, JSON/XML, Web Services, Schnittstellendesign",
                 fachrichtungen: [.AE], stunden: 80),
        Lernfeld(number: 9,
                 title: "Informationssysteme und Netzwerke entwerfen",
                 description: "Netzwerkarchitektur, Datenbankserver, Middleware, Microservices",
                 fachrichtungen: [.AE], stunden: 80),
        Lernfeld(number: 10,
                 title: "Benutzerschnittstellen gestalten und entwickeln",
                 description: "UI/UX-Design, Web-Frontend, Native Apps, Barrierefreiheit, Responsive Design",
                 fachrichtungen: [.AE], stunden: 80),
        Lernfeld(number: 11,
                 title: "Funktionalit\u{00e4}t in Anwendungen realisieren",
                 description: "OOP-Konzepte, Entwurfsmuster, Unit-Tests, Versionsverwaltung (Git)",
                 fachrichtungen: [.AE], stunden: 80),
        Lernfeld(number: 12,
                 title: "Softwareprojekte durchf\u{00fc}hren",
                 description: "Agiles Projektmanagement, Scrum, Kanban, Qualit\u{00e4}tssicherung, DevOps",
                 fachrichtungen: [.AE], stunden: 40)
    ]

    static func getLernfelderForFachrichtung(_ fachrichtung: Fachrichtung) -> [Lernfeld] {
        allLernfelder.filter { lf in
            lf.fachrichtungen.contains(.GEMEINSAM) || lf.fachrichtungen.contains(fachrichtung)
        }
    }
}
