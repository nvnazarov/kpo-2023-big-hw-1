package cinema.logic

class DefaultState : IUseState {
    override fun run(): IUseState? {
        val command = readln()

        val args = command.split(" ");

        when (args[0]) {
            "films" -> showFilms(args)
            "exit" -> return null
        }

        return this
    }

    private fun showFilms(args: List<String>) {
        println("Showing films...")
    }
}