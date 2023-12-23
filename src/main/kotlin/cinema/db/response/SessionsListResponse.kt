package cinema.db.response

import cinema.db.models.Session

/** Response with list of sessions. */
class SessionsListResponse(
    status: Status,
    val sessions: List<Session>
): Response(status)