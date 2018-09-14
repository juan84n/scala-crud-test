package foxhound.application

import foxhound.domain.repository.MessageRepo
import foxhound.domain.service.MessageService.{ findMessage, listMessages }
import foxhound.infrastructure.shared.JsonMapper

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import cats.instances.future._
import play.api.Logger
import play.api.cache.NamedCache
import play.api.cache.redis.CacheAsyncApi

class MessageController @Inject() ( cc: ControllerComponents, msgRepo: MessageRepo )
  extends AbstractController( cc ) with JsonMapper {

  def list(): Action[AnyContent] = Action.async {
    listMessages().run( msgRepo ).fold(
      error => {
        Logger.error( error )
        InternalServerError( error ) as TEXT
      },
      msgs => Ok( Json.toJson( msgs ) ) as JSON
    )
  }

  def find( id: Long ): Action[AnyContent] = Action.async {
    findMessage( id ).run( msgRepo ).fold(
      error => InternalServerError( error ) as TEXT,
      result => result.fold( NotFound as TEXT )( msg => Ok( Json.toJson( msg ) ) as JSON )
    )
  }

}