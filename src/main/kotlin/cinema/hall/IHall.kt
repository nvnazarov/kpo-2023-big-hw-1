package cinema.hall

import cinema.db.models.Ticket

interface IHall {
    /**
     * Prints preview of hall seats, marked as free/sold/confirmed
     * corresponding to the provided [tickets] list.
     */
    fun previewSeats(tickets: List<Ticket>)

    /** Returns whether hall has a seat with [row] and [column]. */
    fun hasSeatAt(row: Int, column: Int): Boolean
}