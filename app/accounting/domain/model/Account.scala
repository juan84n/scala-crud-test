package accounting.domain.model

case class Account(
  id:         String,
  dateOfOpen: String,
  balance:    Double = 0
)
