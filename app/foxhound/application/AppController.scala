package foxhound.application

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future

class AppController @Inject() ( cc: ControllerComponents ) extends AbstractController( cc ) {

  def status: Action[AnyContent] = Action.async {
    Future.successful( Ok( "This is Nastasha Romanenko." ) as TEXT )
  }

}