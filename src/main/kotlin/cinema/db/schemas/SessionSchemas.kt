package cinema.db.schemas

import java.time.LocalDateTime

class SessionAddSchema(
    val filmId: Int,
    val startsAt: LocalDateTime,
)

class SessionRescheduleSchema(
    val sessionId: Int,
    val startsAt: LocalDateTime,
)