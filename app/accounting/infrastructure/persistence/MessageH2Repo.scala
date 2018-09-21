package accounting.infrastructure.persistence

import java.util.concurrent.Executors
import javax.inject.Inject

import accounting.domain.model.Account
import accounting.domain.repository.AccountRepository
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.dbio.Effect.Write
import slick.jdbc.JdbcProfile

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }

/**
 * Created by juannana on 2018/09/19.
 */
class MessageH2Repo @Inject() ( @NamedDatabase( "default" ) val dbProvider:DatabaseConfigProvider ) extends AccountRepository {

  private val dbConfig = dbProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._
  import accounting.infrastructure.persistence.tables.AccountTable.accountTable

  implicit val ec = ExecutionContext.fromExecutorService( Executors.newFixedThreadPool( 15 ) )

  def list(): Future[List[Account]] = {
    db.run( accountTable.result ).map( _.toList )
  }

  def find( id: String ): Future[Option[Account]] = {
    val query = accountTable.filter( acc => acc.id === id )
    db.run( query.result ).map( _.headOption )
  }

  def save( acco: Account ): Future[String] = {

    val response = for {
      res <- accountTable.filter( acc => acc.id === acco.id ).update( acco )
      i <- res match {
        case 0 => accountTable += acco
        case n => DBIO.successful( n )
      }
    } yield i

    db.run( response ).map( resp => s"La cuenta ${acco.id} ha sido agregado/modificado satisfactoriamente")
  }

  def findAll(): Future[List[Account]] = {
    db.run( accountTable.result ).map( _.toList )
  }

  def delete( id: String ): Future[Option[String]] = {
    val query = accountTable.filter( acc => acc.id === id )
    db.run( query.delete ).map( resul => {
      resul match {
        case 1 => Some( s"Cuenta ${id} eliminada satisfactoriamente" )
        case n => None
      }
    } )
  }

}
