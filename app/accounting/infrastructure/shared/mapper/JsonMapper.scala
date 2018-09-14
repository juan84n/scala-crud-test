package accounting.infrastructure.shared.mapper

import accounting.domain.model.{ CheckingAccount, SavingsAccount }
import play.api.libs.json.{ Json, OFormat }

trait JsonMapper {

  implicit val savingAccountFormat: OFormat[SavingsAccount] = Json.format[SavingsAccount]

  implicit val checkingAccountFormat: OFormat[CheckingAccount] = Json.format[CheckingAccount]

}
