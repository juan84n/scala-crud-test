package foxhound.domain

case class Message( id: Long, offset: String, sender: String, content: String )
