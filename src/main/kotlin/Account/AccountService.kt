package Account

class AccountService(
    private val movements: Movements,
    private val accounts: Accounts
) {
    fun deposit(accountId: String, amount: Int) {
        val movement = Movement(accountId, amount)
        movements.append(movement)

        val account = accounts.findBy(accountId)
        val updatedAccount = account.updateBalance(amount)
        accounts.save(updatedAccount);
    }

    fun withdraw(accountId: String, amount: Int) {
        if (balance(accountId) < amount) return

        val movement = Movement(accountId, -amount)
        movements.append(movement)

        val account = accounts.findBy(accountId)
        val updatedAccount = account.updateBalance(-amount)
        accounts.save(updatedAccount);
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

}
