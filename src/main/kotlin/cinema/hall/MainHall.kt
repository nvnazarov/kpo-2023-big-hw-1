package cinema.hall

import cinema.db.models.Ticket

class MainHall : IHall {
    private val seatsInRow: Array<Int> = arrayOf(8, 10, 12)
    private val pattern: String = "     SCREEN#1  ___ __ ___\n2 ____ __ ____\n3 ____________"

    override fun previewSeats(tickets: List<Ticket>) {
        val mark = fun(row: Int, column: Int): String {
            val t = tickets.firstOrNull { it.seatRow == row && it.seatColumn == column } ?: return "_"
            return if (t.confirmed) "o" else "x"
        }

        var row = 1
        var column = 1
        for (char in pattern) {
            when (char) {
                '_' -> {
                    print(mark(row, column))
                    column += 1
                }
                '#' -> println()
                '\n' -> {
                    row += 1
                    column = 1
                    println()
                }

                else -> print(char)
            }
        }
        println()
    }

    override fun hasSeatAt(row: Int, column: Int): Boolean {
        return 1 <= row && row <= seatsInRow.size && 1 <= column && column <= seatsInRow[row - 1]
    }
}