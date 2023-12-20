package cinema.db.models

class Ticket(
    val id: Int,
    val sessionId: Int,
    val seatRow: Int,
    val seatColumn: Int,
    val confirmed: Boolean
)