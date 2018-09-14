package accounting.domain.repository

import java.sql.Date

import accounting.application.context.context
import accounting.domain.model.{ Department, Manager }
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import scala.concurrent.{ ExecutionContext, Future }

class DepartmentRepo @Inject() ( @NamedDatabase( "employees" ) protected val dbConfigProvider:DatabaseConfigProvider ) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  implicit val ec: ExecutionContext = context.executionContext

  import dbConfig._
  import profile.api._

  class DepartmentTable( tag: Tag ) extends Table[Department]( tag, "departments" ) {
    def dept_no = column[String]( "dept_no" )
    def dept_name = column[String]( "dept_name" )

    def * = ( dept_no, dept_name ) <> ( ( Department.apply _ ).tupled, Department.unapply )
  }

  class ManagerTable( tag: Tag ) extends Table[Manager]( tag, "dept_manager" ) {
    def emp_no = column[String]( "emp_no" )
    def dept_no = column[String]( "dept_no" )
    def from_date = column[Date]( "from_date" )
    def to_date: Rep[Date] = column[Date]( "to_date" )

    def * = ( emp_no, dept_no, from_date, to_date ) <> ( ( Manager.apply _ ).tupled, Manager.unapply )
  }

  private val deparments = TableQuery[DepartmentTable]
  private val managers = TableQuery[ManagerTable]

  private val joinedManagerAndDepts = managers join deparments on ( _.dept_no === _.dept_no )

  def findDepartmentByNo( no: String ): Future[Option[Department]] =
    db.run( deparments.filter( _.dept_no === no ).result.headOption )

  def findManagerByEmpNo( empNo: String ): Future[Option[Manager]] =
    db.run( managers.filter( _.emp_no === empNo ).result.headOption )

  case class Detalle( empleNo: String, deptoNo: String, deptoName: String )

  def findDetailByEmpNo( empNo: String ): Future[Option[Detalle]] = {

    val action = for {
      registros <- joinedManagerAndDepts
        .filter { case ( manager, depto ) => manager.emp_no === empNo }
        .result.headOption
    } yield registros.map { case ( manager, depto ) => Detalle( manager.emp_no, depto.dept_no, depto.dept_name ) }

    db run action
  }

}
