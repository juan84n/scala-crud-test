package accounting.domain.repository.impl.inmemory

import java.sql.Date
import accounting.domain.model.CheckingAccount
import accounting.domain.repository.AccountRepository
import scala.collection.mutable.{ Map => MMap }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountRepositoryInMemory extends AccountRepository {

  private val repository: MMap[String, CheckingAccount] = MMap.empty[String, CheckingAccount]

  def find( accountNo: String ): Future[Option[CheckingAccount]] = Future {
    repository.get( accountNo )
  }

  def store( account: CheckingAccount ): Future[CheckingAccount] = Future {
    repository += ( ( account.no, account ) )
    account
  }

  def query( openedOn: Date ): Future[Seq[CheckingAccount]] = Future {
    repository.values.filter( acc => acc.dateOfOpen.contains( openedOn ) ).toSeq
  }

  def all: Future[Seq[CheckingAccount]] = Future {
    repository.values.toSeq
  }

}

object AccountRepositoryInMemory extends AccountRepositoryInMemory