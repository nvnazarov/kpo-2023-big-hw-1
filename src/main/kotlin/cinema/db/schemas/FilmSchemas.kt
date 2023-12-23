package cinema.db.schemas

class FilmAddSchema(
    val title: String,
    val duration: Int,
)

class FilmEditSchema(
    val filmId: Int,
    val title: String,
    val duration: Int,
)