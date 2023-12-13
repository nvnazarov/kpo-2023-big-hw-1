package cinema.db.response

import cinema.db.models.Ticket

class TicketsResponse(
    override val status: Status,
    val tickets: List<Ticket>
) : IResponse