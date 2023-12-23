package cinema.db.response

/** Base class for all responses of the repository. */
abstract class Response(
    /** Response status. */
    val status: Status,
)