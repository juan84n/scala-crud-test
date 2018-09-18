package accounting.infrastructure.shared

import accounting.domain.model.{ Account, BusinessError, TechnicalError }
import play.api.libs.json.{ Json, OFormat, OWrites, Writes }

trait JsonMapper {

  implicit val accountFormat: OFormat[Account] = Json.format[Account]

  implicit val businessErrorWrites: OWrites[BusinessError] = Json.writes[BusinessError]

  implicit val technicalErrorWrites: Writes[TechnicalError] = Writes[TechnicalError]( err =>
    Json.obj(
      "id" -> err.id,
      "msg" -> err.msg,
      "exception" -> err.ex.fold( "" )( exception => exception.getMessage )
    ) )

}
