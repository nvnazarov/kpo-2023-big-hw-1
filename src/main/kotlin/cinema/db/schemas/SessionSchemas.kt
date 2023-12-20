package cinema.db.schemas

class SessionCreate(
    val filmId: Int,
    val startsAt: Int,
)

class SessionUpdate(
    val startAt: Int,
)

class SessionDelete(
    val sessionId: Int,
)