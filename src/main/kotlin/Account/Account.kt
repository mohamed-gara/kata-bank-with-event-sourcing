package Account

class Account(
    val accountId: String,
) {
    fun addMovement(movement: Movement){
        TODO("Not yet implemented")
    }
}

data class Movement(
    val accountId: String,
    val amount: Int
) {

}

