package account

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EventDispatcherAsyncTest {
    // next: use Thread.sleep(100)
    // refactor: maybe we can enhance the code!

    val sut = EventDispatcherAsync()


    @Test
    fun `dispatch event asynchronously`() {
        var mock = Mock()
        sut.register(mock::append)

        sut.dispatch(MovementEvent("", Movement(10)))
        Thread.sleep(500) // TODO Mohamed will find a better solution :) ou pas

        assertThat(mock.event).isEqualTo(MovementEvent("", Movement(10)))
    }
}

class Mock{
    var event: MovementEvent? = null

    fun append(param: MovementEvent){
        this.event = param
    }
}

