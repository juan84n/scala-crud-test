package foxhound.infrastructure.shared

import foxhound.domain.Message
import play.api.libs.json.{ Json, OFormat }

trait JsonMapper {

  implicit val messageFormat: OFormat[Message] = Json.format[Message]

}
