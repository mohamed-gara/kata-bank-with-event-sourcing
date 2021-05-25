package Account

import org.junit.jupiter.api.Test

class EventDispatcherAsyncTest{
    // next: use Thread.sleep(100)
    // refactor: maybe we can enhance the code!

    val sut = EventDispatcherAsync()

    @Test
    fun `test`() {
        sut.register {}
        sut.dispatch(MovementEvent("", Movement(10)))
    }
}