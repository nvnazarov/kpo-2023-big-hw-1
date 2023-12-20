package cinema.db.response

import cinema.db.models.Ticket

class TicketsResponse(
    status: Status,
    val tickets: List<Ticket>
) : Response(status)