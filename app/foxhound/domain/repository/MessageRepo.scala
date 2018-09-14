package foxhound.domain.repository

import foxhound.domain.Message
import scala.concurrent.Future

trait MessageRepo {

  def list(): Future[List[Message]]

  def find( id: Long ): Future[Option[Message]]

}
