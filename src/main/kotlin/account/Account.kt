package account

data class Account(
  val id: String,
  val balance: Int,
) {

}

data class AccountId(
  private val id: String
){

}

data class Movement(
  val amount: Int
) {

}
