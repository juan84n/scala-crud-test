package accounting.domain.model

import accounting.domain.common.{ today, Safe }
import java.sql.Date
import cats.Applicative
import cats.implicits._

case class Account(
  no:         String,
  dateOfOpen: String,
  balance:    Double = 0
)

object Account {

}