package accounting.domain.repository

import accounting.domain.model.Account

import scala.collection.mutable
import scala.concurrent.Future

trait AccountRepository {

  def find( no: String ): Future[Option[Account]]

  def save( acc: Account ): Future[String]

}

class AccountRepoInMemory() extends AccountRepository {

  var accs: mutable.Map[String, Account] = mutable.Map[String, Account](
    "1100" -> Account( "1100", "2018-09-18" )
  )

  def find( no: String ): Future[Option[Account]] = {
    Future.successful( accs.get( no ) )
  }

  def save( acc: Account ): Future[String] = {
    if ( acc.no == "9999" ) {
      Future.failed( new Exception( "Error de conexión a la base de datos" ) )
    } else {
      Future.successful( "Cuenta almacenada exitosamente" )
    }
  }

}