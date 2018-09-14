package accounting.infrastructure.module

import javax.inject.Inject
import play.api.Logger
import play.api.http.{ DefaultHttpRequestHandler, HttpConfiguration, HttpErrorHandler, HttpFilters }
import play.api.mvc.{ Handler, RequestHeader }
import play.api.routing.Router

class RequestHandler @Inject() (
  errorHandler:  HttpErrorHandler,
  configuration: HttpConfiguration,
  filters:       HttpFilters,
  router:        Router
) extends DefaultHttpRequestHandler( router, errorHandler, configuration, filters ) {

  override def routeRequest( request: RequestHeader ): Option[Handler] = {

    Logger.info( "RequestHandler... (ok)" )
    super.routeRequest( request )
  }

}