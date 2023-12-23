package cinema

import cinema.csv.CsvRepository
import cinema.db.IRepository
import cinema.db.response.Status
import cinema.db.schemas.*
import cinema.hall.*
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class App {
    private val repository: IRepository = CsvRepository()
    private val hall: IHall = MainHall()

    fun run() {
        println("Welcome!")

        while (true) {
            print(">>> ")
            val command = readln().trim()

            when (command) {
                "films" -> showFilms()
                "add film" -> addFilm()
                "edit film" -> editFilm()

                "sessions" -> showSessions()
                "add session" -> addSession()
                "reschedule session" -> rescheduleSession()

                "tickets" -> showTickets()
                "add ticket" -> addTicket()
                "cancel ticket" -> cancelTicket()
                "confirm ticket" -> confirmTicket()

                "seats" -> showSeats()

                "exit" -> {
                    println("Good bye!")
                    return
                }

                else -> println("[Error]: unknown command")
            }
        }
    }

    /** Adds new film. */
    private fun addFilm() {
        val title: String
        val duration: Int

        try {
            print("* title: ")
            title = readln()
            if (title.contains(',')) {
                println("[Error]: commas are prohibited in title")
                return
            }
            print("* duration (minutes): ")
            duration = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        if (duration <= 0) {
            println("[Error]: duration must be at least 1 minute long")
            return
        }

        val response = repository.addFilm(FilmAddSchema(title, duration))

        when (response.status) {
            Status.OK -> println("Ok")
            else -> println("[Error]: unknown error")
        }
    }

    /** Updates film data. */
    private fun editFilm() {
        val filmId: Int
        val title: String
        val duration: Int

        try {
            print("* film id: ")
            filmId = readln().toInt()
            print("* title: ")
            title = readln()
            if (title.contains(',')) {
                println("[Error]: commas are prohibited in title")
                return
            }
            print("* duration (minutes): ")
            duration = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        if (duration <= 0) {
            println("[Error]: duration is invalid")
            return
        }

        val response = repository.editFilm(FilmEditSchema(filmId, title, duration))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.FILM_DOES_NOT_EXIST -> println("[Error]: film does not exist")
            Status.SESSIONS_OVERLAP -> println("[Error]: some sessions overlap")
            else -> println("[Error]: unknown error")
        }
    }

    /** Shows all films. */
    private fun showFilms() {
        println("fid\tduration\ttitle")
        repository.getAllFilms().films.forEach {
            println("${it.id}\t${it.duration} minutes\t\"${it.title}\"")
        }
    }

    /** Shows all sessions. */
    private fun showSessions() {
        println("sid\tfid\tschedule")
        repository.getAllSessions().sessions.forEach {
            println("${it.id}\t${it.filmId}\t${it.startsAt}")
        }
    }

    /** Creates new session. */
    private fun addSession() {
        val filmId: Int
        val startsAt: LocalDateTime

        try {
            print("* film id: ")
            filmId = readln().toInt()
            print("* starts at (format: year-month-dayThh:mm[:ss]): ")
            startsAt = LocalDateTime.parse(readln())
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        } catch (e: DateTimeParseException) {
            println("[Error]: invalid time format")
            return
        }

        val response = repository.addSession(SessionAddSchema(filmId, startsAt))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.FILM_DOES_NOT_EXIST -> println("[Error]: film does not exist")
            Status.SESSIONS_OVERLAP -> println("[Error]: session overlaps")
            else -> println("[Error]: unknown error")
        }
    }

    /** Reschedules session. */
    private fun rescheduleSession() {
        val sessionId: Int
        val startsAt: LocalDateTime

        try {
            print("* session id: ")
            sessionId = readln().toInt()
            print("* starts at: ")
            startsAt = LocalDateTime.parse(readln())
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        } catch (e: DateTimeParseException) {
            println("[Error]: invalid time format")
            return
        }

        val response = repository.rescheduleSession(SessionRescheduleSchema(sessionId, startsAt))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.SESSION_DOES_NOT_EXIST -> println("[Error]: session does not exist")
            Status.SESSIONS_OVERLAP -> println("[Error]: session overlaps")
            Status.SESSION_HAS_STARTED -> println("[Error]: cannot reschedule started session")
            else -> println("[Error]: unknown error")
        }
    }

    /** Shows all tickets. */
    private fun showTickets() {
        println("tid\tsid\trow\tcol\tconfirmed")
        repository.getAllTickets().tickets.forEach {
            println("${it.id}\t${it.sessionId}\t${it.seatRow}\t${it.seatColumn}\t${it.confirmed}")
        }
    }

    /** Registers new ticket. */
    private fun addTicket() {
        val sessionId: Int
        val seatRow: Int
        val seatColumn: Int

        try {
            print("* session id: ")
            sessionId = readln().toInt()
            print("* seat row: ")
            seatRow = readln().toInt()
            print("* seat column: ")
            seatColumn = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        if (!hall.hasSeatAt(seatRow, seatColumn)) {
            println("[Error]: hall does not have this seat")
            return
        }

        val response = repository.addTicket(TicketAddSchema(sessionId, seatRow, seatColumn))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.SESSION_DOES_NOT_EXIST -> println("[Error]: session does not exist")
            Status.SESSION_HAS_STARTED -> println("[Error]: session already started")
            Status.SEAT_IS_OCCUPIED -> println("[Error]: seat is occupied")
            else -> println("[Error]: unknown error")
        }
    }

    /** Cancels ticket. */
    private fun cancelTicket() {
        val ticketId: Int

        try {
            print("* ticket id: ")
            ticketId = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        val response = repository.cancelTicket(TicketCancelSchema(ticketId))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.TICKET_DOES_NOT_EXIST -> println("[Error]: ticket does not exist")
            Status.SESSION_HAS_STARTED -> println("[Error]: session has started")
            else -> println("[Error]: unknown error")
        }
    }

    /** Confirms ticket. */
    private fun confirmTicket() {
        val ticketId: Int

        try {
            print("* ticket id: ")
            ticketId = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        val response = repository.confirmTicket(TicketConfirmSchema(ticketId))

        when (response.status) {
            Status.OK -> println("Ok")
            Status.TICKET_DOES_NOT_EXIST -> println("[Error]: ticket does not exist")
            Status.TICKET_IS_ALREADY_CONFIRMED -> println("[Error]: ticket is already confirmed")
            else -> println("[Error]: unknown error")
        }
    }

    /** Shows sold and free seats at given session. */
    private fun showSeats() {
        val sessionId: Int

        try {
            print("* session id: ")
            sessionId = readln().toInt()
        } catch (e: NumberFormatException) {
            println("[Error]: expected integer")
            return
        }

        val response = repository.getTicketsForSession(TicketsGetForSessionSchema(sessionId))

        when (response.status) {
            Status.OK -> {}
            Status.SESSION_DOES_NOT_EXIST -> {
                println("[Error]: session does not exist")
                return
            }
            else -> {
                println("[Error]: unknown error")
                return
            }
        }

        hall.previewSeats(response.tickets)
    }
}