package cinema.db.schemas

class FilmCreate(
    val title: String,
    val duration: Int,
)

class FilmUpdate(
    val filmId: Int,
    val title: String,
    val duration: Int,
)

class FilmDelete(
    val filmId: Int,
)