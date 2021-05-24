package Account

// TODO:
// version 3 : update read model on write asynchronously WIP
// Fix test with deposit sync for test (define interface)
// Add test for asynchronous ?

// version 3.0.1: use async for dispatchAsync
// version 3.1: rehydrate aggregate for computing balance on the fly
class AccountService(
    private val accounts: Accounts,
    private val store: EventStore
) {
    fun deposit(accountId: String, amount: Int) {

        val event = MovementEvent(accountId, Movement(amount))
        store.append(event)

    }

    fun withdraw(accountId: String, amount: Int) {
        val existingAccount = accounts.findBy(accountId)

        if (existingAccount.balance < amount) return

        val event = MovementEvent(accountId, Movement(-amount))
        store.append(event)

    }

    fun balance(accountId: String): Int =
        accounts.findBy(accountId).balance

    fun findMovements(accountId: String): List<Movement> =
        store.movementsBy(accountId)
}

class Accounts(
    private val accounts: MutableMap<String, Account> = mutableMapOf()
) {
    fun findBy(accountId: String): Account =
        accounts[accountId] ?: Account(accountId, 0)

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
        dispatchAsync(event)
    }

    fun register(listener: EventListener) {
        listeners.add(listener)
    }

    fun dispatchAsync(event: MovementEvent) {
        Thread {
            dispatch(event)
        }.start()
    }

    private fun dispatch(event: MovementEvent) {
        listeners.forEach {
            it.apply { this(event) }
        }
    }

    fun movementsBy(accountId: String): List<Movement> =
        events.filter { it.accountId == accountId }
            .map { it.movement }

}


class BalanceCalculator(
    private val accounts: Accounts
) {

    fun updateBalance(event: MovementEvent) {
        val currentAccount = this.accounts.findBy(event.accountId)
        val updatedAccount = currentAccount.copy(balance = currentAccount.balance + event.movement.amount)
        accounts.save(updatedAccount)
    }
}

data class MovementEvent(
    val accountId: String,
    val movement: Movement
) {

}
