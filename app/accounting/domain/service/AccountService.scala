package accounting.domain.service

import accounting.domain.repository.AccountRepository
import accounting.domain.model.Account
import cats.data.Reader

import scala.collection.mutable
import scala.concurrent.Future

object AccountService {

  def buscarCuenta( no: String ): Reader[AccountRepository, Future[Option[Account]]] = Reader {
    ( repo ) =>
      {
        repo.find( no )
      }
  }

  def guardarCuenta( acc: Account ): Reader[AccountRepository, Future[String]] = Reader {
    ( repo ) =>
      {
        repo.save( acc )
      }
  }

  def listarCuentas(): Reader[AccountRepository, Future[List[Account]]] = Reader {
    ( repo ) =>
      {
        repo.findAll()
      }
  }

  def eliminarCuenta( no: String ): Reader[AccountRepository, Future[Option[String]]] = Reader {
    ( repo ) =>
      {
        repo.delete( no )
      }
  }

}
