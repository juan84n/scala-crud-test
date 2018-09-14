package accounting.infrastructure.shared.mapper.types

trait XmlParseable[A] {

  def toXml( value: A ): String
  def fromXml( value: String ): A

}

object XmlParseable {
  def toXml[A]( value: A )( implicit p: XmlParseable[A] ): String = {
    p.toXml( value )
  }

  def fromXml[A]( value: String )( implicit p: XmlParseable[A] ): A = {
    p.fromXml( value )
  }
}
