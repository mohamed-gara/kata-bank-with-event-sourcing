package Account

typealias CommandResult<T> = Pair<T, List<Movement>>

data class Account(
    val accountId: String,
    val balance: Int
) {
    fun deposit(amount: Int): CommandResult<Account> {
        val updateAccount = updateBalance(amount)
        val movement = Movement(accountId, amount)
        return CommandResult(updateAccount, listOf(movement))
    }

    fun withdraw(amount: Int): CommandResult<Account> {
        if (balance < amount) return Pair(this, listOf())

        val updateAccount = updateBalance(-amount)
        val movement = Movement(accountId, -amount)
        return Pair(updateAccount, listOf(movement))
    }

    private fun updateBalance(amount: Int): Account =
        Account(this.accountId, this.balance + amount)
}

data class Movement(
    val accountId: String,
    val amount: Int
) {

}
