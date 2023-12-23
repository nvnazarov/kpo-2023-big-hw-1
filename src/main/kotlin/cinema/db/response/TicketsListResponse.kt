package cinema.db.response

import cinema.db.models.Ticket

/** Response with list of tickets. */
class TicketsListResponse(
    status: Status,
    val tickets: List<Ticket>
) : Response(status)