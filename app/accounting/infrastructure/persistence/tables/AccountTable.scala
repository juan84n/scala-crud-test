package accounting.infrastructure.persistence.tables

import accounting.domain.model.Account
import slick.jdbc.MySQLProfile.api._

/**
 * Created by juannana on 2018/09/19.
 */
object AccountTable {

  val accountTable = TableQuery[AccountTable]

  class AccountTable( tag: Tag ) extends Table[Account]( tag, "tbl_account" ) {
    def id = column[String]( "id" )
    def dateOfOpen = column[String]( "dateOfOpen" )
    def balance = column[Double]( "balance" )

    val pk = primaryKey( "tbl_account_pk", id )

    override def * = ( id, dateOfOpen, balance ) <> ( Account.tupled, Account.unapply )
  }

}
