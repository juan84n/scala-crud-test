package accounting.domain.repository

import accounting.domain.model.CheckingAccount
import java.sql.Date
import scala.concurrent.Future

trait AccountRepository {

  def find( accountNo: String ): Future[Option[CheckingAccount]]

  def query( openedOn: Date ): Future[Seq[CheckingAccount]]

  def all: Future[Seq[CheckingAccount]]

}
