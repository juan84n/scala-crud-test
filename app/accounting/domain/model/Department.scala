package accounting.domain.model

import play.api.libs.json.Json

case class Department(
  dept_no:   String,
  dept_name: String
)

object Department {
  implicit val formatter = Json.format[Department]
}
