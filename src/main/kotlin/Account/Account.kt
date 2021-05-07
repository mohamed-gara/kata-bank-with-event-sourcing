package Account

data class Account(
  val accountId: String,
  val balance: Int,

  val movements: List<Movement>
) {
  fun deposit(amount: Int): Account = updateBalance(amount)

  fun withdraw(amount: Int): Account {
    if (balance < amount) return this

    return updateBalance(-amount)
  }

  private fun updateBalance(amount: Int): Account =
    copy(
      balance = this.balance + amount,
      movements = this.movements + Movement(amount)
    )
}

data class Movement(
  val amount: Int
) {

}
