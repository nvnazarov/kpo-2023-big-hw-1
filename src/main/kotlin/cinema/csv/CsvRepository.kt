package cinema.csv

import cinema.db.IRepository
import cinema.db.response.*
import cinema.db.schemas.*
import cinema.db.models.*
import java.io.File
import java.time.LocalDateTime

class CsvRepository : IRepository {
    private val filmsPath = "db/Films.csv"
    private val sessionsPath = "db/Sessions.csv"
    private val ticketsPath = "db/Tickets.csv"

    private val films: MutableList<Film> = File(filmsPath).inputStream().readFilmsCsv()
    private val sessions: MutableList<Session> = File(sessionsPath).inputStream().readSessionsCsv()
    private val tickets: MutableList<Ticket> = File(ticketsPath).inputStream().readTicketsCsv()

    /** Saves films into file. */
    private fun commitFilms() {
        File(filmsPath).outputStream().writeFilmsCsv(films)
    }

    /** Saves sessions into file. */
    private fun commitSessions() {
        File(sessionsPath).outputStream().writeSessionsCsv(sessions)
    }

    /** Saves tickets into file. */
    private fun commitTickets() {
        File(ticketsPath).outputStream().writeTicketsCsv(tickets)
    }

    /** Returns all films stored in repository. */
    override fun getAllFilms(): FilmsListResponse {
        return FilmsListResponse(
            Status.OK,
            films
        )
    }

    /** Creates new film and stores in repository. */
    override fun addFilm(film: FilmAddSchema): StatusResponse {
        val filmId = films.maxOfOrNull { it.id + 1 } ?: 0
        films.add(Film(filmId, film.title, film.duration))
        commitFilms()

        return StatusResponse(Status.OK)
    }

    /** Updates film data. */
    override fun editFilm(film: FilmEditSchema): StatusResponse {
        // check if film id is invalid
        if (films.none { it.id == film.filmId }) {
            return StatusResponse(Status.FILM_DOES_NOT_EXIST)
        }

        val targetFilm = films.first { it.id == film.filmId }

        // check sessions overlap
        for (session in sessions) {
            if (session.filmId == film.filmId) {
                for (checkSession in sessions) {
                    if (checkSession.startsAt < session.startsAt) {
                        continue
                    }
                    if (checkSession.startsAt <= session.startsAt.plusMinutes(targetFilm.duration.toLong())) {
                        return StatusResponse(Status.SESSIONS_OVERLAP)
                    }
                }
            }
        }

        // update film
        films.remove(targetFilm)
        films.add(Film(film.filmId, film.title, film.duration))
        commitFilms()

        return StatusResponse(Status.OK)
    }

    /** Returns all sessions stored in repository. */
    override fun getAllSessions(): SessionsListResponse {
        return SessionsListResponse(Status.OK, sessions);
    }

    /** Creates new session and stores in repository. */
    override fun addSession(session: SessionAddSchema): StatusResponse {
        if (films.none { it.id == session.filmId }) {
            return StatusResponse(Status.FILM_DOES_NOT_EXIST)
        }

        val targetFilm = films.first { it.id == session.filmId }
        val sessionEnd = session.startsAt.plusMinutes(targetFilm.duration.toLong())

        // check sessions overlap
        for (checkSession in sessions) {
            val checkFilm = films.first { it.id == checkSession.filmId }
            if (checkSession.startsAt > sessionEnd ||
                checkSession.startsAt.plusMinutes(checkFilm.duration.toLong()) < session.startsAt) {
                continue
            }
            return StatusResponse(Status.SESSIONS_OVERLAP)
        }

        val sessionId = sessions.maxOfOrNull { it.id + 1 } ?: 0
        sessions.add(Session(sessionId, session.filmId, session.startsAt))
        commitSessions()

        return StatusResponse(Status.OK)
    }

    /** Reschedules session. */
    override fun rescheduleSession(session: SessionRescheduleSchema): StatusResponse {
        // check if session id is invalid
        if (sessions.none { it.id == session.sessionId }) {
            return StatusResponse(Status.SESSION_DOES_NOT_EXIST)
        }

        // check if session has started
        if (sessions.first { it.id == session.sessionId }.startsAt <= LocalDateTime.now()) {
            return StatusResponse(Status.SESSION_HAS_STARTED)
        }

        val targetSession = sessions.first { it.id == session.sessionId }
        val targetFilm = films.first { it.id == targetSession.filmId }
        val sessionEnd = session.startsAt.plusMinutes(targetFilm.duration.toLong())

        // check sessions overlap
        for (checkSession in sessions) {
            if (checkSession.id == session.sessionId) {
                continue
            }
            val checkFilm = films.first { it.id == checkSession.filmId }
            if (checkSession.startsAt > sessionEnd ||
                checkSession.startsAt.plusMinutes(checkFilm.duration.toLong()) < session.startsAt) {
                continue
            }
            return StatusResponse(Status.SESSIONS_OVERLAP)
        }

        // update session
        val filmId = sessions.find { it.id == session.sessionId }!!.filmId
        sessions.removeIf { it.id == session.sessionId }
        sessions.add(Session(session.sessionId, filmId, session.startsAt))
        commitSessions()

        return StatusResponse(Status.OK)
    }

    /** Returns all tickets stored in repository. */
    override fun getAllTickets(): TicketsListResponse {
        return TicketsListResponse(Status.OK, tickets)
    }

    /** Creates new ticket and stores in repository. */
    override fun addTicket(ticket: TicketAddSchema): StatusResponse {
        // validate session
        if (sessions.none { it.id == ticket.sessionId }) {
            return StatusResponse(Status.SESSION_DOES_NOT_EXIST)
        }
        val session = sessions.first { it.id == ticket.sessionId }
        if (session.startsAt <= LocalDateTime.now()) {
            return StatusResponse(Status.SESSION_HAS_STARTED)
        }

        // validate seat (must not be occupied)
        if (tickets.any { it.sessionId == ticket.sessionId &&
                    it.seatRow == ticket.seatRow && it.seatColumn == ticket.seatColumn}) {
            return StatusResponse(Status.SEAT_IS_OCCUPIED)
        }

        // add ticket
        val ticketId = tickets.maxOfOrNull { it.id + 1 } ?: 0
        tickets.add(Ticket(ticketId, ticket.sessionId, ticket.seatRow, ticket.seatColumn))
        commitTickets()

        return StatusResponse(Status.OK)
    }

    /** Cancels ticket. */
    override fun cancelTicket(ticket: TicketCancelSchema): StatusResponse {
        // check if ticket id is invalid
        if (tickets.none { it.id == ticket.ticketId }) {
            return StatusResponse(Status.TICKET_DOES_NOT_EXIST)
        }

        val t = tickets.first { it.id == ticket.ticketId }

        // check if session has started
        if (sessions.first { it.id == t.sessionId }.startsAt <= LocalDateTime.now()) {
            return StatusResponse(Status.SESSION_HAS_STARTED)
        }

        // delete ticket
        tickets.remove(t)
        commitTickets()

        return StatusResponse(Status.OK)
    }

    /** Confirms ticket. */
    override fun confirmTicket(ticket: TicketConfirmSchema): StatusResponse {
        // check if ticket id is invalid
        if (tickets.none { it.id == ticket.ticketId }) {
            return StatusResponse(Status.TICKET_DOES_NOT_EXIST)
        }

        val t = tickets.first { it.id == ticket.ticketId }

        // check if already confirmed
        if (t.confirmed) {
            return StatusResponse(Status.TICKET_IS_ALREADY_CONFIRMED)
        }

        // update ticket
        tickets.remove(t)
        tickets.add(Ticket(t.id, t.sessionId, t.seatRow, t.seatColumn, true))
        commitTickets()

        return StatusResponse(Status.OK)
    }

    /** Returns tickets associated with session. */
    override fun getTicketsForSession(session: TicketsGetForSessionSchema): TicketsListResponse {
        // check if session id is invalid
        if (sessions.none { it.id == session.sessionId }) {
            return TicketsListResponse(
                Status.SESSION_DOES_NOT_EXIST,
                listOf()
            )
        }

        // return all found tickets
        return TicketsListResponse(
            Status.OK,
            tickets.filter { it.sessionId == session.sessionId }
        )
    }
}