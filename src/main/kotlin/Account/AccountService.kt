package Account

import kotlinx.coroutines.runBlocking

// TODO:
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
