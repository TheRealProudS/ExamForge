package de.sysitprep.app.data.model

/**
 * Represents a Lernfeld (learning field) as defined by the IHK curriculum.
 */
data class Lernfeld(
    val number: Int,
    val title: String,
    val description: String,
    val fachrichtungen: List<Fachrichtung>,
    val stunden: Int
)

object LernfelderData {

    val allLernfelder: List<Lernfeld> = listOf(
        // ── Gemeinsame Lernfelder (LF 1–7) ──────────────────────────────────────
        Lernfeld(1, "Das Unternehmen und die eigene Rolle im Betrieb beschreiben",
            "Unternehmensorganisation, Berufsausbildung, Rechte und Pflichten, Betriebsklima",
            listOf(Fachrichtung.GEMEINSAM), 40),
        Lernfeld(2, "Arbeitsplätze nach Kundenwunsch ausstatten",
            "Hardware, Betriebssysteme, Softwarelizenzen, IT-Systeme auswählen und installieren",
            listOf(Fachrichtung.GEMEINSAM), 80),
        Lernfeld(3, "Clients in Netzwerke einbinden",
            "OSI-Modell, TCP/IP, IP-Adressierung, DHCP, DNS, WLAN, grundlegende Netzwerkkonfiguration",
            listOf(Fachrichtung.GEMEINSAM), 80),
        Lernfeld(4, "Schutzbedarfsanalyse im eigenen Arbeitsbereich durchführen",
            "IT-Sicherheit, Datenschutz (DSGVO), Bedrohungsanalyse, Schutzmaßnahmen",
            listOf(Fachrichtung.GEMEINSAM), 80),
        Lernfeld(5, "Software zur Verwaltung von Daten anpassen",
            "Datenbanken, SQL, Datenmodellierung, ER-Diagramme, Normalisierung",
            listOf(Fachrichtung.GEMEINSAM), 80),
        Lernfeld(6, "Serviceanfragen bearbeiten",
            "ITIL, Ticketsystem, First-Level-Support, SLA, Incident-Management",
            listOf(Fachrichtung.GEMEINSAM), 40),
        Lernfeld(7, "Cyber-physische Systeme ergänzen",
            "IoT, Sensoren, Aktoren, Mikrocontroller, Schnittstellen, Protokolle",
            listOf(Fachrichtung.GEMEINSAM), 80),

        // ── Systemintegration – spezifische Lernfelder (LF 8–12 SI) ─────────────
        Lernfeld(8, "Netzwerke und Dienste bereitstellen",
            "Serverinfrastruktur, Active Directory, DNS, DHCP-Server, Routing, Firewalls",
            listOf(Fachrichtung.SI), 80),
        Lernfeld(9, "Netzwerke und Dienste absichern",
            "VPN, Firewall-Konzepte, Verschlüsselung, PKI, Zertifikate, Penetrationstest",
            listOf(Fachrichtung.SI), 80),
        Lernfeld(10, "Clients administrieren und absichern",
            "Gruppenrichtlinien, MDM, Virenschutz, Backup-Strategien, Hardening",
            listOf(Fachrichtung.SI), 80),
        Lernfeld(11, "Komplexe IT-Systeme einrichten",
            "Virtualisierung, Cloud-Dienste, Hochverfügbarkeit, Storage, SAN/NAS",
            listOf(Fachrichtung.SI), 80),
        Lernfeld(12, "IT-Systeme in Betrieb nehmen",
            "Projektplanung, Migration, Rollout, Dokumentation, ITIL-Betrieb",
            listOf(Fachrichtung.SI), 40),

        // ── Anwendungsentwicklung – spezifische Lernfelder (LF 8–12 AE) ─────────
        Lernfeld(8, "Daten systemübergreifend bereitstellen",
            "REST-APIs, JSON/XML, Web Services, Schnittstellendesign",
            listOf(Fachrichtung.AE), 80),
        Lernfeld(9, "Informationssysteme und Netzwerke entwerfen",
            "Netzwerkarchitektur, Datenbankserver, Middleware, Microservices",
            listOf(Fachrichtung.AE), 80),
        Lernfeld(10, "Benutzerschnittstellen gestalten und entwickeln",
            "UI/UX-Design, Web-Frontend, Native Apps, Barrierefreiheit, Responsive Design",
            listOf(Fachrichtung.AE), 80),
        Lernfeld(11, "Funktionalität in Anwendungen realisieren",
            "OOP-Konzepte, Entwurfsmuster, Unit-Tests, Versionsverwaltung (Git)",
            listOf(Fachrichtung.AE), 80),
        Lernfeld(12, "Softwareprojekte durchführen",
            "Agiles Projektmanagement, Scrum, Kanban, Qualitätssicherung, DevOps",
            listOf(Fachrichtung.AE), 40)
    )

    fun getLernfelderForFachrichtung(fachrichtung: Fachrichtung): List<Lernfeld> {
        return allLernfelder.filter {
            it.fachrichtungen.contains(fachrichtung) ||
            it.fachrichtungen.contains(Fachrichtung.GEMEINSAM)
        }
    }
}
