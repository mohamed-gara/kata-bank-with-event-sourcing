package Account

class Account(
    val accountId: String,
) {
    fun addMovement(movement: Movement){
        TODO("Not yet implemented")
    }
}

data class Movement(
    val amount: Int,
    val accountId: String
) {

}

