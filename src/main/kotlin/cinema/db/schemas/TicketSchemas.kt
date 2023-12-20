package cinema.db.schemas

class TicketCreate(
    val sessionId: Int,
    val seatRow: Int,
    val seatColumn: Int,
)

class TicketUpdate(
    val seatRow: Int,
    val seatColumn: Int,
    val confirmed: Boolean,
)

class TicketDelete(
    val ticketId: Int,
)

class TicketsGetForSession(
    val sessionId: Int
)