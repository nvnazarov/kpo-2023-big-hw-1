package cinema.db.models

import java.time.LocalDateTime

class Session(
    val id: Int,
    val filmId: Int,
    val startsAt: LocalDateTime,
)