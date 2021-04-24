package Account

class Account(
  val accountId: String,
  val events: List<Event> = listOf()
) {
  fun addMovement(movement: Movement):  {
    TODO("Not yet implemented")
  }
}

class Movement(
  val amount: Int
) {
  class Event {

  }

}

