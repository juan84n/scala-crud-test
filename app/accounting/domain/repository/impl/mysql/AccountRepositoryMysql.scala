package accounting.domain.repository.impl.mysql

import java.sql.Date
import accounting.domain.model.CheckingAccount
import accounting.domain.repository.AccountRepository
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import scala.concurrent.{ ExecutionContext, Future }
import accounting.application.context.context.executionContext

class AccountRepositoryMysql @Inject() ( @NamedDatabase( "frdm" ) protected val dbConfigProvider:DatabaseConfigProvider )
  extends AccountRepository {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  implicit val ec: ExecutionContext = executionContext

  import dbConfig._
  import profile.api._

  class CheckingAccountTable( tag: Tag ) extends Table[CheckingAccount]( tag, "accounts" ) {
    def no = column[String]( "no" )
    def name = column[String]( "name" )
    def date_of_open = column[Option[Date]]( "date_of_open" )
    def date_of_close = column[Option[Date]]( "date_of_close" )
    def balance = column[Double]( "balance" )

    def * = ( no, name, date_of_open, date_of_close, balance ) <> ( ( CheckingAccount.apply _ ).tupled, CheckingAccount.unapply )
  }

  private val accounts = TableQuery[CheckingAccountTable]

  private def _findByNo( no: String ): DBIO[Option[CheckingAccount]] =
    accounts.filter( _.no === no ).result.headOption

  private def _findByDate( date: Date ): Query[CheckingAccountTable, CheckingAccount, List] =
    accounts.filter( _.date_of_open === date ).to[List]

  def find( accountNo: String ): Future[Option[CheckingAccount]] =
    db.run( _findByNo( accountNo ) )

  def query( openedOn: Date ): Future[Seq[CheckingAccount]] =
    db.run( _findByDate( openedOn ).result )

  def all: Future[Seq[CheckingAccount]] =
    db.run( accounts.to[List].result )

}

