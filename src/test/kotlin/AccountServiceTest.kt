import Account.AccountService
import Account.Accounts
import Account.Movement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

internal class AccountServiceTest {

  @Test
  fun `deposit money amount in account`() {
    val accounts = Accounts()
    val sut = AccountService(accounts)
    val accountId = "account_id"

    sut.deposit(accountId, 100)

    assertThat(accounts.events).contains(Movement(100, accountId))
  }
}
