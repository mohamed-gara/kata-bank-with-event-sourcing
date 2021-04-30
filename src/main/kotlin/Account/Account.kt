package Account

data class Account(
    val accountId: String,
    val balance: Int
) {
    fun updateBalance(amount: Int): Account {
        return Account(this.accountId, this.balance + amount)
    }

}

data class Movement(
    val accountId: String,
    val amount: Int
) {

}

