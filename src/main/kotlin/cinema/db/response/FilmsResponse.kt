package cinema.db.response

import cinema.db.models.Film

class FilmsResponse(
    status: Status,
    val films: List<Film>
) : Response(status)