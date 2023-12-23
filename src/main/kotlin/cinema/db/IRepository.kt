package cinema.db

import cinema.db.schemas.*
import cinema.db.response.*

/** Interface for cinema app repository */
interface IRepository {
    fun getAllFilms(): FilmsListResponse
    fun addFilm(film: FilmAddSchema): StatusResponse
    fun editFilm(film: FilmEditSchema): StatusResponse
    fun getAllSessions(): SessionsListResponse
    fun addSession(session: SessionAddSchema): StatusResponse
    fun rescheduleSession(session: SessionRescheduleSchema): StatusResponse
    fun getAllTickets(): TicketsListResponse
    fun addTicket(ticket: TicketAddSchema): StatusResponse
    fun cancelTicket(ticket: TicketCancelSchema): StatusResponse
    fun confirmTicket(ticket: TicketConfirmSchema): StatusResponse
    fun getTicketsForSession(session: TicketsGetForSessionSchema): TicketsListResponse
}