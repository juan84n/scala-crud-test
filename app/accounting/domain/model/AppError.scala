package accounting.domain.model

sealed trait AppError {
  def id: String
  def msg: String
}

case class BusinessError( id: String, msg: String ) extends AppError

case class TechnicalError( id: String, msg: String, ex: Option[Throwable] ) extends AppError