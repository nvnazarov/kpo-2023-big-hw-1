package cinema.db

import cinema.db.schemas.*
import cinema.db.response.*

/** Interface for cinema app repository */
interface IRepository {
    fun createFilm(film: FilmCreate): StatusResponse
    fun updateFilm(film: FilmUpdate): StatusResponse
    fun deleteFilm(film: FilmDelete): StatusResponse

    fun createSession(session: SessionCreate): StatusResponse
    fun updateSession(session: SessionUpdate): StatusResponse
    fun deleteSession(session: SessionDelete): StatusResponse

    fun createTicket(ticket: TicketCreate): StatusResponse
    fun updateTicket(ticket: TicketUpdate): StatusResponse
    fun deleteTicket(ticket: TicketDelete): StatusResponse

    fun getTicketsForSession(session: TicketsGetForSession): TicketsResponse
}