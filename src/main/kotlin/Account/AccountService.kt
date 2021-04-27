package Account

class AccountService(
    private val movements: Movements
) {
    fun deposit(accountId: String, amount: Int) {
        val movement = Movement(amount, accountId)
        movements.append(movement)
    }

    fun withdraw(accountId: String, amount: Int) {
        val movement = Movement(-amount, accountId)
        movements.append(movement)
    }
}

class Movements(
    val movements: MutableList<Movement> = mutableListOf()
) {
    fun append(movement: Movement) {
        movements.add(movement)
    }
}
