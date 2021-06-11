package account

import java.util.*

class UserService (private val users: Users){
    fun create(name: String): String {
        val id = UUID.randomUUID().toString()
        users.save(User(id, name))
        return id
    }

    fun findBy(id: String): User? =
        users.findBy(id)
}

class Users(
    private val  users : MutableMap<String, User> = mutableMapOf()
) {
    fun findBy(userId: String): User? =
        users[userId]

    fun save(user: User) {
        users[user.id] = user
    }
}

data class User(
    val id: String,
    val name: String
) {

}

data class UserId(
    private val id: String
) {

}