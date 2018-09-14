package accounting.infrastructure.shared.mapper.types

trait CsvParseable[A] {
  def toCsv( value: A ): String

  def fromCsv( value: String ): A
}

object CsvParseable {
  def toCsv[A]( value: A )( implicit p: CsvParseable[A] ): String = {
    p.toCsv( value )
  }

  def fromCsv[A]( value: String )( implicit p: CsvParseable[A] ): A = {
    p.fromCsv( value )
  }
}

