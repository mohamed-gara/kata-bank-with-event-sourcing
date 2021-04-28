package Account

class AccountService(
    private val movements: Movements
) {
    fun deposit(accountId: String, amount: Int) {
        val movement = Movement(accountId, amount)
        movements.append(movement)
    }

    fun withdraw(accountId: String, amount: Int) {
        if (balance(accountId) < amount) return

        val movement = Movement(accountId, -amount)
        movements.append(movement)
    }

    fun balance(accountId: String): Int {
        return movements.movements
            .filter { it.accountId == accountId }
            .sumOf { it.amount }
    }
}

class Movements(
    val movements: MutableList<Movement> = mutableListOf()
) {
    fun append(movement: Movement) {
        movements.add(movement)
    }
}
