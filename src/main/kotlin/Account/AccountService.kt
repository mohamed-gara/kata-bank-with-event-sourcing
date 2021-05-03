package Account

class AccountService(
    private val movements: Movements,
    private val accounts: Accounts
) {
    fun deposit(accountId: String, amount: Int) {
        val movement = Movement(accountId, amount)
        movements.append(movement)
        accounts.apply(movement)
    }

    fun withdraw(accountId: String, amount: Int) {
        if (balance(accountId) < amount) return

        val movement = Movement(accountId, -amount)
        movements.append(movement)
        accounts.apply(movement)
    }

    fun balance(accountId: String): Int {
        val account = accounts.findBy(accountId)
        return account.balance
    }
}

class Movements(
    val movements: MutableList<Movement> = mutableListOf()
) {
    fun append(movement: Movement) {
        movements.add(movement)
    }
}

class Accounts(
    val accounts: MutableMap<String, Account> = mutableMapOf()
) {

    fun findBy(accountId: String): Account =
        accounts[accountId] ?: Account(accountId, 0)

    fun save(account: Account) {
        accounts[account.accountId] = account
    }

    fun apply(movement: Movement) {
        val account = findBy(movement.accountId)
        val updatedAccount = account.updateBalance(movement.amount)
        save(updatedAccount);
    }

}
