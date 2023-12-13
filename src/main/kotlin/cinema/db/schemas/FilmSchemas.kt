package cinema.db.schemas

class FilmCreate(
    val title: String,
    val description: String,
    val duration: Int,
)

class FilmUpdate(
    val title: String,
    val description: String,
    val duration: Int,
)

class FilmDelete(
    val filmId: Int,
)