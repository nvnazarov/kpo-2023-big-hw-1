package cinema.db.schemas

class TicketCreate(
    val sessionId: Int,
    val seatRow: Int,
    val setColumn: Int,
)

class TicketUpdate(
    val seatRow: Int,
    val setColumn: Int,
)

class TicketDelete(
    val ticketId: Int,
)

class TicketsGetForSession(
    val sessionId: Int
)