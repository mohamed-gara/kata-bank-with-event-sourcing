package account

import java.util.*

class UserService (private val users: Users){
    fun create(name: String): String {
        val id = UUID.randomUUID().toString()
        users.save(User(id, "Le petit Omar"))
        return id
    }

    fun findBy(id: String): User =  User(id, "Le petit Omar")
}

class Users(
    private val  users : MutableMap<String, User> = mutableMapOf()
) {
    fun findBy(userId: String): User =
        users[userId] ?: User(userId, "Le joker") // TODO remove the joker

    fun save(user: User) {
        users[user.id] = user
    }
}

data class User(
    val id: String,
    val name: String
) {

}
