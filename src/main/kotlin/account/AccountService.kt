package account

import kotlinx.coroutines.runBlocking

// TODO:
// As user I want to deposit money in different accounts
// Use read model (combining user and account) to search accounts
// add snapshot optimisation
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

    fun findWithBalanceBetween(min: Int, max: Int): Collection<Account> =
        accounts.findWithBalanceBetween(min, max)

    fun deposit(userId: String, accountId: String, amount: Int) {
        deposit(accountId, amount)
    }
}

class Accounts(
    private val  accounts: MutableMap<String, Account> = mutableMapOf()
) {
    fun findBy(accountId: String): Account =
        accounts[accountId] ?: Account(accountId, 0)

    fun save(account: Account) {
        accounts[account.id] = account
    }

    fun findWithBalanceBetween(min: Int, max: Int): Collection<Account> = accounts.values.filter {
        it.balance in min..max
    }
}

typealias EventListener = (MovementEvent) -> Unit

class EventStore(
    private val events: MutableList<MovementEvent> = mutableListOf(),
    private val eventDispatcher: EventDispatcher
) {
    fun append(event: MovementEvent) = runBlocking{
        events.add(event)
        eventDispatcher.dispatch(event)
    }

    fun register(listener: EventListener) {
        eventDispatcher.register(listener)
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

interface EventDispatcher {
    fun dispatch(movement: MovementEvent)

    fun register(listener: EventListener)
}

class EventDispatcherSync(
    private val listeners: MutableList<EventListener> = mutableListOf()
) : EventDispatcher {

    override fun dispatch(event: MovementEvent) {
        listeners.forEach {
            it.apply { this(event) }
        }
    }
    override fun register(listener: EventListener) {
        listeners.add(listener)
    }

}

class EventDispatcherAsync(
    private val listeners: MutableList<EventListener> = mutableListOf()
) : EventDispatcher {

    override fun dispatch(event: MovementEvent) {
        Thread.sleep(500)
        Thread {
            listeners.forEach {
                it.apply { this(event) }
            }
        }.start()
    }

    override fun register(listener: EventListener) {
        listeners.add(listener)
    }

}
