package accounting.domain.repository

import accounting.domain.model.Account

import scala.collection.mutable
import scala.concurrent.Future

trait AccountRepository {

  def find( no: String ): Future[Option[Account]]

  def save( acc: Account ): Future[String]

  def findAll(): Future[List[Account]]

  def delete( no: String ): Future[Option[String]]

}

class AccountRepoInMemory() extends AccountRepository {

  var accs: mutable.Map[String, Account] = mutable.Map[String, Account](
    "1100" -> Account( "1100", "2018-09-18" )
  )

  def find( no: String ): Future[Option[Account]] = {
    Future.successful( accs.get( no ) )
  }

  def save( acc: Account ): Future[String] = {
    if ( acc.id == "9999" ) {
      Future.failed( new Exception( "Error de conexión a la base de datos" ) )
    } else {
      accs += ( acc.id -> acc )
      Future.successful( s"Cuenta ${acc.id} almacenada exitosamente" )
    }
  }

  def findAll(): Future[List[Account]] = {
    Future.successful( accs.map( reg => reg._2 ).toList )
  }

  def delete( no: String ): Future[Option[String]] = {
    if ( accs.contains( no ) ) {
      accs -= no
      Future.successful( Some( s"Cuenta ${no} eliminada" ) )
    } else {
      Future.successful( None )
    }
  }

}