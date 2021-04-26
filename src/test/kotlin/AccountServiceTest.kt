import Account.AccountService
import Account.Movements
import Account.Movement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class AccountServiceTest {

  val accounts = Movements()
  val sut = AccountService(accounts)

  val account_id_1 = "account_id_1"
  val account_id_2 = "account_id_2"

  @Test fun `deposit money amount in an empty account`() {

    sut.deposit(account_id_1, 100)

    assertThat(accounts.movements)
      .containsExactly(Movement(100, account_id_1))
  }

  @Test fun `deposit money amount in a non empty account`() {

    accounts.movements.add(Movement(100, account_id_1))

    sut.deposit(account_id_1, 230)

    assertThat(accounts.movements)
      .containsExactly(
        Movement(100, account_id_1),
        Movement(230, account_id_1)
      )
  }

  @Test fun `deposit money amount in two empty accounts`() {

    sut.deposit(account_id_1, 100)
    sut.deposit(account_id_2, 200)

    assertThat(accounts.movements)
      .containsExactly(
        Movement(100, account_id_1),
        Movement(200, account_id_2)
      )
  }
}
