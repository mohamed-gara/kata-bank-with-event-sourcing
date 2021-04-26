package Account

class AccountService(
    val accounts: Accounts // TODO use event store
) {
    fun deposit(accountId: String, amount: Int) {

        val movement = Movement(amount, accountId)
        accounts.append(movement)
    }
}

class Accounts(
    val events: MutableList<Movement> = mutableListOf()
) {

    fun append(movement: Movement) {
        events.add(movement)
    }

}
