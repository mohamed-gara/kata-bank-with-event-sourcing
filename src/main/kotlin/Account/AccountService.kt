package Account

// TODO:
// version 2.3 : use event dispatcher
// version 3 : update read model on write asynchronously
class AccountService(
    private val accounts: Accounts,
    private val store: EventStore
) {
    fun deposit(accountId: String, amount: Int) {

        val event = MovementEvent(accountId, Movement(amount))
        store.append(event)

        val existingAccount = accounts.findBy(accountId)
        val updatedAccount = existingAccount.deposit(amount)

        accounts.save(updatedAccount)
    }

    fun withdraw(accountId: String, amount: Int) {
        val existingAccount = accounts.findBy(accountId)
        val updatedAccount = existingAccount.withdraw(amount)

        if (existingAccount === updatedAccount) return

        accounts.save(updatedAccount)
    }

    fun balance(accountId: String): Int =
        accounts.findBy(accountId).balance

    fun findMovements(accountId: String): List<Movement> =
        accounts.findBy(accountId).movements
}

class Accounts(
    private val accounts: MutableMap<String, Account> = mutableMapOf()
) {
    fun findBy(accountId: String): Account =
        accounts[accountId] ?: Account(accountId, 0, listOf())

    fun save(account: Account) {
        accounts[account.accountId] = account
    }
}


typealias EventListener = (MovementEvent) -> Unit

class EventStore(
    private val events: MutableList<MovementEvent> = mutableListOf(),
    private val listeners: MutableList<EventListener> = mutableListOf()
) {
    fun append(event: MovementEvent) {
        events.add(event)
        dispatch(event)
    }

    fun register(listener: EventListener) {
        listeners.add(listener)
    }

    private fun dispatch(event: MovementEvent) {
        listeners.forEach {
            it.apply { event }
        }
    }
}


class BalanceCalculator(
    private val store: EventStore
) {
    // TODO implement
}

data class MovementEvent(
    val accountId: String,
    val movement: Movement
) {

}
