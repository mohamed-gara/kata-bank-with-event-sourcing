package Account

// TODO:
// version 2.3 : use event dispatcher
// version 3 : update read model on write asynchronously
class AccountService(
  private val accounts: Accounts
) {
  fun deposit(accountId: String, amount: Int) {

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
