package accounting.application

import java.util.concurrent.Executors
import accounting.domain.model.Account
import accounting.domain.repository.AccountRepository
import accounting.infrastructure.shared.JsonMapper
import javax.inject.Inject
import play.api.libs.json.{ JsValue, Json }
import play.api.Logger
import play.api.mvc._
import scala.concurrent.{ ExecutionContext, Future }
import accounting.domain.service.AccountService

class AccountController @Inject() ( cc: ControllerComponents, accountRepo: AccountRepository ) extends AbstractController( cc ) with JsonMapper {

  implicit val ec = ExecutionContext.fromExecutorService( Executors.newFixedThreadPool( 15 ) )

  def status(): Action[AnyContent] = Action {
    Logger.info( "BaseController ... /status  (ok)" )
    Ok( "We're up and running" ) as TEXT
  }

  def find( no: String ): Action[AnyContent] = Action.async {
    accountRepo.find( no )
      .map { resultado =>
        resultado match {
          case Some( account ) => Ok( Json.toJson( account ) ) as JSON
          case None            => NotFound( "No encontramos la cuenta" ) as TEXT
        }
      }
  }

  def save(): Action[AnyContent] = Action.async { request =>

    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case None => Future.successful( BadRequest( "No recibimos un json válido" ) as TEXT )
      case Some( body ) => {
        val receivedAccount: Account = body.as[Account]
        accountRepo.save( receivedAccount )
          .map( resultado => Ok( resultado ) as TEXT )
      }
    }

  }

  def findWithReader( no: String ): Action[AnyContent] = Action.async {

    // Paso 1. Configurar el Reader
    val myConfiguredReader = AccountService.buscarCuenta( no )

    // Paso 2. Ejecutarlo
    myConfiguredReader.run( accountRepo )
      .map { resultado =>
        resultado match {
          case Some( account ) => Ok( Json.toJson( account ) ) as JSON
          case None            => NotFound( "No encontramos la cuenta" ) as TEXT
        }
      }

  }

  def saveWithReader(): Action[AnyContent] = Action.async { request =>

    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case None => Future.successful( BadRequest( "No recibimos un json válido" ) as TEXT )
      case Some( body ) => {
        val receivedAccount: Account = body.as[Account]
        val myConfiguredReader = AccountService.guardarCuenta( receivedAccount )
        myConfiguredReader.run( accountRepo )
          .map { resultado => Ok( resultado ) as TEXT }
      }
    }
  }

  def findAllWithReader = Action.async {

    // Paso 1. Configurar el Reader
    val myConfiguredReader = AccountService.listarCuentas()

    // Paso 2. Ejecutarlo
    myConfiguredReader.run( accountRepo )
      .map { resultado => Ok( Json.toJson( resultado ) ) as JSON }

  }

  def deleteWithReader( no: String ): Action[AnyContent] = Action.async {

    val myConfiguredReader = AccountService.eliminarCuenta( no )

    myConfiguredReader.run( accountRepo )
      .map { resultado =>
        resultado match {
          case Some( info ) => Ok( Json.toJson( info ) ) as JSON
          case None         => NotFound( "No encontramos la cuenta" ) as TEXT
        }
      }

  }

}