package accounting.domain.service

import accounting.domain.common.{ AccountType, SafeResp }
import accounting.domain.repository.AccountRepository
import java.util.Date
import cats.data.Kleisli
import scala.concurrent.Future

trait AccountService[Account, Amount, Balance] {

  type ServiceOp[A] = Kleisli[Future, AccountRepository, SafeResp[A]]

  def open( no: String, name: String, rate: Option[Double], openingDate: Option[Date], accountType: AccountType ): ServiceOp[Account]

  def close( no: String, closeDate: Option[Date] ): ServiceOp[Account]

  def debit( no: String, amount: Amount ): ServiceOp[Account]

  def credit( no: String, amount: Amount ): ServiceOp[Account]

  def balance( no: String ): ServiceOp[Balance]
}
