package accounting.infrastructure.shared.mapper

import java.text.SimpleDateFormat
import java.sql.Date
import accounting.domain.model.{ CheckingAccount, SavingsAccount }
import accounting.infrastructure.shared.mapper.types.CsvParseable
import scala.util.Try

trait CsvMapper {

  implicit val checkingAccountCsvMapper: CsvParseable[CheckingAccount] = new CsvParseable[CheckingAccount] {

    override def toCsv( account: CheckingAccount ): String = {
      val csvAccount: String = s"%s,%s,%s,%s,%s".format(
        account.no,
        account.name,
        account.dateOfOpen.getOrElse( "" ).toString,
        account.dateOfClose.getOrElse( "" ).toString,
        account.balance
      )
      csvAccount
    }

    override def fromCsv( value: String ): CheckingAccount = {
      val fields: Array[String] = value.split( "," )

      val openDate: Try[Date] = Try( Date.valueOf( fields( 2 ) ) )
      val closeDate: Try[Date] = Try( Date.valueOf( fields( 3 ) ) )
      val balance: Try[Double] = Try( fields( 4 ).toDouble )

      CheckingAccount( fields( 0 ), fields( 1 ), openDate.toOption, closeDate.toOption, balance.getOrElse( 0D ) )
    }

  }

  implicit val savingsAccountCsvMapper: CsvParseable[SavingsAccount] = new CsvParseable[SavingsAccount] {

    override def toCsv( account: SavingsAccount ): String = {
      val csvAccount: String = s"%s,%s,%s,%s,%s,%s".format(
        account.no,
        account.name,
        account.rateOfInterest,
        account.dateOfOpen.getOrElse( "" ).toString,
        account.dateOfClose.getOrElse( "" ).toString,
        account.balance
      )
      csvAccount
    }

    override def fromCsv( value: String ): SavingsAccount = {
      val fields = value.split( "," )
      val format: SimpleDateFormat = new SimpleDateFormat()
      val openDate: Try[Date] = Try( Date.valueOf( fields( 3 ) ) )
      val closeDate: Try[Date] = Try( Date.valueOf( fields( 4 ) ) )
      val balance: Try[Double] = Try( fields( 5 ).toDouble )
      val rateOfInterest: Try[Double] = Try( fields( 2 ).toDouble )

      SavingsAccount( fields( 0 ), fields( 1 ), rateOfInterest.getOrElse( 0D ), openDate.toOption, closeDate.toOption, balance.getOrElse( 0D ) )
    }

  }

}
