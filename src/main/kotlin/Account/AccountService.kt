package Account

// TODO:
// version 2.2 : save events in aggregate ???
// version 2.3 : use event dispatcher
// version 3 : update read model on write asynchronously
class AccountService(
    private val movements: Movements,
    private val accounts: Accounts
) {
    fun deposit(accountId: String, amount: Int) {

        val existingAccount = accounts.findBy(accountId)
        val (updatedAccount, events) = existingAccount.deposit(amount)

        accounts.save(updatedAccount)
        movements.append(events)
    }

    fun withdraw(accountId: String, amount: Int) {
        val existingAccount = accounts.findBy(accountId)
        val (updatedAccount, events) = existingAccount.withdraw(amount)

        if (events.isEmpty()) return

        accounts.save(updatedAccount)
        movements.append(events)
    }

    fun balance(accountId: String): Int = accounts.findBy(accountId).balance
}

class Movements(
    val movements: MutableList<Movement> = mutableListOf()
) {
    fun append(movement: Movement) {
        movements.add(movement)
    }

    fun append(movements: List<Movement>) {
        this.movements.addAll(movements)
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
