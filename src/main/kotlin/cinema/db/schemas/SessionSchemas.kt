package cinema.db.schemas

class SessionCreate(
    val filmId: Int,
    val startAt: Int,
)

class SessionUpdate(
    val startAt: Int,
)

class SessionDelete(
    val sessionId: Int,
)