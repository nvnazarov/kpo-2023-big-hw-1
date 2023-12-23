package cinema.db.response

import cinema.db.models.Film

/** Response with list of films. */
class FilmsListResponse(
    status: Status,
    val films: List<Film>
) : Response(status)