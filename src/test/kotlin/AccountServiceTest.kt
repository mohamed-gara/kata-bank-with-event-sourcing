import Account.AccountService
import Account.Movements
import Account.Movement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


internal class AccountServiceTest {

  val accounts = Movements()
  val sut = AccountService(accounts)

  val account_id_1 = "account_id_1"
  val account_id_2 = "account_id_2"

  @Nested
  inner class deposit{

    @Test fun `deposit in an empty account`() {

      sut.deposit(account_id_1, 100)

      assertThat(accounts.movements)
        .containsExactly(Movement(account_id_1, 100))
    }

    @Test fun `deposit in a non empty account`() {

      accounts.movements.add(Movement(account_id_1, 100))

      sut.deposit(account_id_1, 230)

      assertThat(accounts.movements)
        .containsExactly(
          Movement(account_id_1, 100),
          Movement(account_id_1, 230)
        )
    }

    @Test fun `deposit in two empty accounts`() {

      sut.deposit(account_id_1, 100)
      sut.deposit(account_id_2, 200)

      assertThat(accounts.movements)
        .containsExactly(
          Movement(account_id_1, 100),
          Movement(account_id_2, 200)
        )
    }

  }

  @Nested
  inner class withdraw{

    @Test fun `withdraw in a non empty account`(){
      sut.deposit(account_id_1, 100)

      sut.withdraw(account_id_1, 50)

      assertThat(accounts.movements)
        .containsExactly(
          Movement(account_id_1, 100),
          Movement(account_id_1, -50)
        )
    }

    @Test fun `withdraw in empty account`(){
      sut.withdraw(account_id_1, 50)

      assertThat(accounts.movements)
        .containsExactly(
          Movement(account_id_1, -50)
        )
    }

    @Test fun `withdraw in two empty account`(){
      sut.withdraw(account_id_1, 50)
      sut.withdraw(account_id_2, 100)

      assertThat(accounts.movements)
        .containsExactly(
          Movement(account_id_1, -50),
          Movement(account_id_2, -100)
        )
    }


  }



}
