package foxhound.infrastructure.persistence.repository

import foxhound.domain.Message
import foxhound.domain.repository.MessageRepo
import javax.inject.Inject
import play.api.cache.{ AsyncCacheApi, NamedCache }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MessageCacheRepo @Inject() ( @NamedCache( "foxhound-cache" ) cache:AsyncCacheApi ) extends MessageRepo {

  def list(): Future[List[Message]] = {
    cache.get[List[Message]]( "messages" )
      .map( res => res.getOrElse( List.empty[Message] ) )
  }

  def find( id: Long ): Future[Option[Message]] = {
    cache.get[List[Message]]( "messages" )
      .map( res => res.flatMap( msgs => msgs.find( _.id == id ) ) )
  }

}
