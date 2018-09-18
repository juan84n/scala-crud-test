package accounting.domain.service

import accounting.domain.repository.AccountRepository
import accounting.domain.model.Account
import cats.data.Reader
import scala.concurrent.Future

object AccountService {

  def buscarCuenta( no: String ): Reader[AccountRepository, Future[Option[Account]]] = Reader {
    ( repo ) =>
      {
        repo.find( no )
      }
  }

}
