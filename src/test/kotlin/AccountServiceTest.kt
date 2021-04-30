import Account.AccountService
import Account.Accounts
import Account.Movement
import Account.Movements
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class AccountServiceTest {

    val movements = Movements()
    val accounts = Accounts()
    val sut = AccountService(movements, accounts)

    val account_id_1 = "account_id_1"
    val account_id_2 = "account_id_2"

    @Nested
    inner class deposit {

        @Test
        fun `deposit in an empty account`() {

            sut.deposit(account_id_1, 100)

            assertThat(movements.movements)
                .containsExactly(Movement(account_id_1, 100))
        }

        @Test
        fun `deposit in a non empty account`() {

            movements.movements.add(Movement(account_id_1, 100))

            sut.deposit(account_id_1, 230)

            assertThat(movements.movements)
                .containsExactly(
                    Movement(account_id_1, 100),
                    Movement(account_id_1, 230)
                )
        }

        @Test
        fun `deposit in two empty accounts`() {

            sut.deposit(account_id_1, 100)
            sut.deposit(account_id_2, 200)

            assertThat(movements.movements)
                .containsExactly(
                    Movement(account_id_1, 100),
                    Movement(account_id_2, 200)
                )
        }
    }

    @Nested
    inner class withdraw {

        @Test
        fun `withdraw in a non empty account`() {
            sut.deposit(account_id_1, 100)

            sut.withdraw(account_id_1, 50)

            assertThat(movements.movements)
                .containsExactly(
                    Movement(account_id_1, 100),
                    Movement(account_id_1, -50)
                )
        }

        @Nested
        inner class ForbiddenOperations {
            @Test
            fun `withdraw is forbidden if not enough money`() {
                sut.withdraw(account_id_1, 50)

                assertThat(movements.movements).isEmpty()
            }

            @Test
            fun `withdraw is forbidden if not enough money in user account`() {
                sut.deposit(account_id_2, 50)

                sut.withdraw(account_id_1, 50)

                assertThat(movements.movements)
                    .containsExactly(
                        Movement(account_id_2, 50),
                    )
            }

            @Test
            fun `withdraw is forbidden if no more money in user account after a first withdraw`() {
                sut.deposit(account_id_1, 50)

                sut.withdraw(account_id_1, 50)
                sut.withdraw(account_id_1, 50)

                assertThat(movements.movements)
                    .containsExactly(
                        Movement(account_id_1, 50),
                        Movement(account_id_1, -50),
                    )
            }
        }
    }

    @Nested
    inner class balance {

        @Test
        fun `balance is 10 after one deposit of 10`() {
            sut.deposit(account_id_1, 10)

            val result = sut.balance(account_id_1)

            assertThat(result).isEqualTo(10)
        }

        @Test
        fun `balance is 30 after two deposit of 10 and 20 in the same account`() {
            sut.deposit(account_id_1, 10)
            sut.deposit(account_id_1, 20)

            val result = sut.balance(account_id_1)

            assertThat(result).isEqualTo(30)
        }

        @Test
        fun `balance is 20 in the second account after deposit of 10 in a first account and 20 in a second account`() {
            sut.deposit(account_id_1, 10)
            sut.deposit(account_id_2, 20)

            val result = sut.balance(account_id_2)

            assertThat(result).isEqualTo(20)
        }
    }
}
