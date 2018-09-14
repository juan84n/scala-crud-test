package accounting.infrastructure.module

import javax.inject.{ Inject, Provider, Singleton }
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.{ BAD_REQUEST, FORBIDDEN, NOT_FOUND }
import play.api.mvc.Results.InternalServerError
import play.api.mvc.{ RequestHeader, Result }
import play.api.routing.Router
import play.api._
import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject() (
  env:          Environment,
  config:       Configuration,
  sourceMapper: OptionalSourceMapper,
  router:       Provider[Router]
) extends DefaultHttpErrorHandler( env, config, sourceMapper, router ) {

  override def onClientError( request: RequestHeader, statusCode: Int, message: String ): Future[Result] = {

    Logger.info( "ErrorHandler onClientError... (ok)" )

    statusCode match {
      case BAD_REQUEST => onBadRequest( request, message )
      case FORBIDDEN => onForbidden( request, message )
      case NOT_FOUND => onNotFound( request, message )
      case clientError if statusCode >= 400 && statusCode < 500 => onOtherClientError( request, statusCode, message )
      case nonClientError =>
        throw new IllegalArgumentException( s"onClientError invoked with non client error status code $statusCode: $message" )
    }

  }

  override def onProdServerError( request: RequestHeader, exception: UsefulException ): Future[Result] = {

    Logger.info( "ErrorHandler onServerError... (ok)" )
    Future.successful( InternalServerError( views.html.defaultpages.error( exception ) ) )

  }

}
