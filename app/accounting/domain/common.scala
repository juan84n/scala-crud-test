package accounting.domain

import java.sql.Date
import java.time.LocalDate
import cats.data.ValidatedNel

object common {

  type Safe[A] = ValidatedNel[String, A]
  type SafeResp[A] = Either[String, A]

  def today: Date = Date.valueOf( LocalDate.now )

  sealed trait AccountType
  case object Checking extends AccountType
  case object Savings extends AccountType

}
