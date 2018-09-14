package accounting.application.controller

import accounting.application.context.context
import accounting.infrastructure.module.Greeting
import javax.inject.Inject
import play.api.{ Configuration, Logger }
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

class BaseController @Inject() ( cc: ControllerComponents, configuration: Configuration, greeting: Greeting ) extends AbstractController( cc ) {

  implicit val ec: ExecutionContext = context.executionContext

  def status(): Action[AnyContent] = Action {

    Logger.info( "BaseController ... /status  (ok)" )

    Ok( "We're up and running" ) as TEXT

  }

  def sayHello( name: String ): Action[AnyContent] = Action.async {

    Logger.info( "BaseController ... /sayHello  (ok)" )

    val msg = greeting.sayHello( name )
    Future.successful( Ok( msg ) as TEXT )

  }

}