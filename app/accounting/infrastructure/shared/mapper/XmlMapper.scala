package accounting.infrastructure.shared.mapper

import accounting.domain.model.{ CheckingAccount, SavingsAccount }
import accounting.infrastructure.shared.mapper.types.XmlParseable
import java.text.SimpleDateFormat
import java.sql.Date
import scala.util.Try
import scala.xml.{ Elem, XML }

trait XmlMapper {

  implicit val checkingAccountXmlMapper: XmlParseable[CheckingAccount] = new XmlParseable[CheckingAccount] {
    override def toXml( account: CheckingAccount ): String = {
      val xmlAccount: Elem =
        <account>
          <no>{ account.no }</no>
          <name> { account.name } </name>
          <dateOfOpen>{ account.dateOfOpen.getOrElse( "" ) }</dateOfOpen>
          <dateOfClose>{ account.dateOfClose.getOrElse( "" ) }</dateOfClose>
          <balance>{ account.balance }</balance>
        </account>
      xmlAccount.toString().stripMargin
    }

    override def fromXml( value: String ): CheckingAccount = {
      val xml: Elem = XML.loadString( value )
      val dateFormat: SimpleDateFormat = new SimpleDateFormat()

      val openDate: Try[Date] = Try( Date.valueOf( ( xml \ "dateOfOpen" ).text ) )
      val closeDate: Try[Date] = Try( Date.valueOf( ( xml \ "dateOfClose" ).text ) )
      val balance: Try[Double] = Try( ( xml \ "amount" ).text.toDouble )

      CheckingAccount( ( xml \ "no" ).text, ( xml \ "name" ).text, openDate.toOption, closeDate.toOption, balance.getOrElse( 0D ) )
    }
  }

  implicit val savingsAccountXmlMapper: XmlParseable[SavingsAccount] = new XmlParseable[SavingsAccount] {
    override def toXml( account: SavingsAccount ): String = {
      val xmlAccount: Elem =
        <account>
          <no>{ account.no }</no>
          <name> { account.name } </name>
          <rateOfInterest> { account.rateOfInterest } </rateOfInterest>
          <dateOfOpen>{ account.dateOfOpen.getOrElse( "" ) }</dateOfOpen>
          <dateOfClose>{ account.dateOfClose.getOrElse( "" ) }</dateOfClose>
          <balance>{ account.balance }</balance>
        </account>
      xmlAccount.toString().stripMargin
    }

    override def fromXml( value: String ): SavingsAccount = {
      val xml: Elem = XML.loadString( value )
      val dateFormat: SimpleDateFormat = new SimpleDateFormat()

      val openDate: Try[Date] = Try( Date.valueOf( ( xml \ "dateOfOpen" ).text ) )
      val closeDate: Try[Date] = Try( Date.valueOf( ( xml \ "dateOfClose" ).text ) )
      val balance: Try[Double] = Try( ( xml \ "amount" ).text.toDouble )
      val rateOfInterest: Try[Double] = Try( ( xml \ "rateOfInterest" ).text.toDouble )

      SavingsAccount( ( xml \ "no" ).text, ( xml \ "name" ).text, rateOfInterest.getOrElse( 0D ), openDate.toOption, closeDate.toOption, balance.getOrElse( 0D ) )
    }
  }

}
