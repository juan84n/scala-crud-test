package foxhound.infrastructure.persistence.repository

import foxhound.domain.Message
import foxhound.domain.repository.MessageRepo
import foxhound.infrastructure.persistence.tables.FoxHoundTables._
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

class MessageDbRepo @Inject() ( @NamedDatabase( "foxhound" ) val dbProvider:DatabaseConfigProvider ) extends MessageRepo {

  private val dbConfig = dbProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  def list(): Future[List[Message]] = {
    db.run( messageTable.result ).map( _.toList )
  }

  def find( id: Long ): Future[Option[Message]] = {
    val query = messageTable.filter( msg => msg.id === id )
    db.run( query.result ).map( _.headOption )
  }

}
