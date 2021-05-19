package Account

data class Account(
  val accountId: String,
  val balance: Int,
) {

}

data class Movement(
  val amount: Int
) {

}
