import Account.Account
import Account.AccountService
import Account.Accounts
import Account.Movement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class AccountServiceTest {

  @Test
  fun `deposit money amount in account`() {
    val accounts = mock(Accounts::class.java)
    val sut = AccountService(accounts)
    val accountId = "account_id"
    val account = Account(accountId)
    `when`(sut.accounts.findBy(accountId)).thenReturn(account)

    sut.deposit(accountId, 100)

    assertThat(account.movements).contains(Movement(100))
  }
}
