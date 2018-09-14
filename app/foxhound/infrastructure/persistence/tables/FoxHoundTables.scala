package foxhound.infrastructure.persistence.tables

import foxhound.domain.Message
import slick.jdbc.MySQLProfile.api._

object FoxHoundTables {

  val messageTable = TableQuery[MessageTable]

  class MessageTable( tag: Tag ) extends Table[Message]( tag, "tbl_message" ) {
    def id = column[Long]( "id", O.AutoInc )
    def offset = column[String]( "msg_offset" )
    def sender = column[String]( "sender" )
    def content = column[String]( "content" )

    val pk = primaryKey( "tbl_message_pk", id )

    override def * = ( id, offset, sender, content ) <> ( Message.tupled, Message.unapply )
  }

}

