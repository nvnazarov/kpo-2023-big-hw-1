package cinema.db.schemas

class TicketAddSchema(
    val sessionId: Int,
    val seatRow: Int,
    val seatColumn: Int,
)

class TicketCancelSchema(
    val ticketId: Int,
)

class TicketConfirmSchema(
    val ticketId: Int,
)

class TicketsGetForSessionSchema(
    val sessionId: Int
)