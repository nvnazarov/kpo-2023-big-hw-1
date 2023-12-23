package cinema.csv

import cinema.db.models.*
import java.io.*
import java.time.LocalDateTime

fun InputStream.readFilmsCsv(): MutableList<Film> {
    val reader = this.bufferedReader()
    return reader
        .lineSequence()
        .filter { it.isNotBlank() }
        .map {
            it.toFilm()
        }.toMutableList()
}

fun InputStream.readSessionsCsv(): MutableList<Session> {
    val reader = this.bufferedReader()
    return reader
        .lineSequence()
        .filter { it.isNotBlank() }
        .map {
            it.toSession()
        }.toMutableList()
}

fun InputStream.readTicketsCsv(): MutableList<Ticket> {
    val reader = this.bufferedReader()
    return reader
        .lineSequence()
        .filter { it.isNotBlank() }
        .map {
            it.toTicket()
        }.toMutableList()
}

fun String.toFilm(): Film {
    val (id, title, duration) = this.split(",")
    return Film(id.toInt(), title, duration.toInt())
}

fun String.toSession(): Session {
    val (id, filmId, startsAt) = this.split(",")
    return Session(id.toInt(), filmId.toInt(), LocalDateTime.parse(startsAt))
}

fun String.toTicket(): Ticket {
    val (id, sessionId, seatRow, seatColumn, confirmed) = this.split(",")
    return Ticket(id.toInt(), sessionId.toInt(), seatRow.toInt(), seatColumn.toInt(), (confirmed == "true"))
}

fun OutputStream.writeFilmsCsv(films: List<Film>) {
    val writer = bufferedWriter()
    films.forEach {
        writer.write("${it.id},${it.title},${it.duration}")
        writer.newLine()
    }
    writer.flush()
}

fun OutputStream.writeSessionsCsv(sessions: List<Session>) {
    val writer = bufferedWriter()
    sessions.forEach {
        writer.write("${it.id},${it.filmId},${it.startsAt}")
        writer.newLine()
    }
    writer.flush()
}

fun OutputStream.writeTicketsCsv(tickets: List<Ticket>) {
    val writer = bufferedWriter()
    tickets.forEach {
        writer.write(
            "${it.id},${it.sessionId},${it.seatRow},${it.seatColumn},${it.confirmed}")
        writer.newLine()
    }
    writer.flush()
}