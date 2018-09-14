package accounting.domain.model

import accounting.domain.common.{ today, Safe }
import java.sql.Date
import cats.Applicative
import cats.implicits._

sealed trait Account {
  def no: String
  def name: String
  def dateOfOpen: Option[Date]
  def dateOfClose: Option[Date]
  def balance: Double
}

case class CheckingAccount(
  no:          String,
  name:        String,
  dateOfOpen:  Option[Date],
  dateOfClose: Option[Date] = None,
  balance:     Double       = 0
) extends Account

case class SavingsAccount(
  no:             String,
  name:           String,
  rateOfInterest: Double,
  dateOfOpen:     Option[Date],
  dateOfClose:    Option[Date] = None,
  balance:        Double       = 0
) extends Account

/* Account Factory */
object Account {

  /* Validation Functions */
  private val validateAccountNo: ( String ) => Safe[String] =
    ( no ) => {
      if ( no.isEmpty || no.length < 11 ) s"Account No has to be at least 11 characters long: found $no".invalidNel
      else no.validNel
    }

  private val validateName: ( String ) => Safe[String] =
    ( name ) => {
      if ( name.isEmpty ) s"The name in the account must not be empty".invalidNel
      else name.validNel
    }

  private val validateOpenCloseDate: ( Date, Option[Date] ) => Safe[Option[Date]] =
    ( od, cd ) => {
      cd.map { c =>
        if ( c before od ) s"Close date [$c] cannot be earlier than open date [$od]".invalidNel
        else Some( od ).validNel
      }.getOrElse { Some( od ).validNel }
    }

  private val validateRate: ( Double ) => Safe[Double] =
    ( rate ) => {
      if ( rate <= 0 ) s"Interest rate $rate must be > 0".invalidNel
      else rate.validNel
    }

  private val validateAccountAlreadyClosed: ( Account ) => Safe[Account] =
    ( account ) => {
      if ( account.dateOfClose.isDefined ) s"Account ${account.no} is already closed".invalidNel
      else account.validNel
    }

  private val validateCloseDate: ( Account, Date ) => Safe[Date] =
    ( account, closeDate ) => {
      if ( closeDate before account.dateOfOpen.get ) s"Close date [$closeDate] cannot be earlier than open date [${account.dateOfOpen.get}]".invalidNel
      else closeDate.validNel
    }

  private val checkBalance: ( Account, Double ) => Safe[Account] =
    ( account, amount ) => {
      if ( amount < 0 && account.balance < -amount ) s"Insufficient amount in ${account.no} to debit".invalidNel
      else account.validNel
    }

  /* Operations on Accounts */
  def close( account: Account, closeDate: Date ): Safe[Account] = {

    Applicative[Safe].map2( validateAccountAlreadyClosed( account ), validateCloseDate( account, closeDate ) ) { ( validAccount, validDate ) =>
      validAccount match {
        case ca: CheckingAccount => ca.copy( dateOfClose = Some( validDate ) )
        case sa: SavingsAccount  => sa.copy( dateOfClose = Some( validDate ) )
      }
    }
  }

  def updateBalance( account: Account, amount: Double ): Safe[Account] = {
    Applicative[Safe].map2( validateAccountAlreadyClosed( account ), checkBalance( account, amount ) ) { ( _, _ ) =>
      account match {
        case ca: CheckingAccount => ca.copy( balance = ca.balance + amount )
        case sa: SavingsAccount  => sa.copy( balance = sa.balance + amount )
      }
    }
  }

  def rate( account: Account ): Option[Double] = account match {
    case SavingsAccount( _, _, rateOfInterest, _, _, _ ) => rateOfInterest.some
    case _ => None
  }

  /* Smart Constructors */
  def checkingAccount(
    no:        String,
    name:      String,
    openDate:  Option[Date],
    closeDate: Option[Date],
    balance:   Double
  ): Safe[CheckingAccount] = {

    val od = openDate.getOrElse( today )

    Applicative[Safe].map3( validateAccountNo( no ), validateName( name ), validateOpenCloseDate( od, closeDate ) ) { ( accNo, accName, accODate ) => CheckingAccount( accNo, accName, accODate, closeDate, balance ) }
  }

  def savingsAccount(
    no:        String,
    name:      String,
    rate:      Double,
    openDate:  Option[Date],
    closeDate: Option[Date],
    balance:   Double
  ): Safe[SavingsAccount] = {

    val od = openDate.getOrElse( today )

    Applicative[Safe].map4( validateAccountNo( no ), validateName( name ), validateOpenCloseDate( od, closeDate ), validateRate( rate ) ) { ( accNo, accName, accODate, accRate ) => SavingsAccount( accNo, accName, accRate, accODate, closeDate, balance ) }
  }

}