package accounting.application.controller

import accounting.application.AppContext
import play.api.libs.json.Json._
import cats.data.OptionT
import accounting.domain.model.CheckingAccount
import javax.inject.Inject
import play.api.mvc._
import cats.implicits._
import accounting.application.context.context.executionContext
import scala.concurrent.{ ExecutionContext, Future }
import accounting.infrastructure.shared.mapper.Mappers
import accounting.infrastructure.shared.mapper.types.XmlParseable.toXml
import accounting.infrastructure.shared.mapper.types.CsvParseable.toCsv

class Controller @Inject() (
  cc:      ControllerComponents,
  context: AppContext
) extends AbstractController( cc ) with Mappers {

  implicit val ec: ExecutionContext = executionContext

  def status() = Action {
    Ok( "..." )
  }

  def account( no: String, format: String ): Action[AnyContent] = Action.async { request =>
    val response = OptionT[Future, CheckingAccount]( context.accountRepoMySql.find( no ) )
    response
      .fold( NotFound( "La cuenta no existe" ) ) { account =>
        format match {
          case "json" => Ok( toJson( account ) ) as JSON
          case "xml"  => Ok( toXml( account ) ) as XML
          case "csv"  => Ok( toCsv( account ) ) as TEXT
          case _      => Ok( toJson( account ) ) as JSON
        }
      }
      .recover { case ex: Exception => InternalServerError( ex.getMessage ) }
  }

}