package user

import account.User
import account.UserService
import account.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class UserServiceTest {

    val users = Users()
    val sut = UserService(users)

    @ParameterizedTest
    @ValueSource(strings=["Le petit Omar", "Le petit Silvère"])
    fun `create and find a user`(userName: String) {
        val userId = sut.create(userName)

        val user = sut.findBy(userId)

        assertThat(user).isEqualTo(
            User(userId, userName)
        )
    }

    @Test
    fun `find an unknown user`(){
        val result = sut.findBy("Le petit Mohamed")

        assertThat(result).isNull()
    }
}

