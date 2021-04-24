package Account

class AccountService(
  val accounts: Accounts // TODO use event store
) {
  fun deposit(accountId: String, amount: Int) {
    val account = accounts.findBy(accountId)

    val updatedAccount = account.addMovement(Movement(amount))

    accounts.save(updatedAccount)
  }
}

class Accounts {
  fun findBy(accountId: String): Account {
    TODO("Not yet implemented")
  }

  fun save(account: Account) {
    TODO("Not yet implemented")
  }
}
