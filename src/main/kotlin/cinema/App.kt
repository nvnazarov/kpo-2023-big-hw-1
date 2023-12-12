package cinema

import cinema.logic.IUseState;
import cinema.logic.DefaultState;

class App {
    private var state: IUseState = DefaultState()

    fun run() {
        while (true) {
            state = state.run() ?: return
        }
    }
}