package accounting.infrastructure.persistence.repository

import akka.Done
import javax.inject.Inject
import play.api.cache.{ AsyncCacheApi, NamedCache }
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CacheRepository @Inject() ( @NamedCache( "db-cache" ) cache:AsyncCacheApi ) {

  def upload(): Future[Done] = {
    cache.set( "documentTypes", List( "CC", "TI" ) )
  }

  def download( key: String ): Future[Option[List[String]]] = {
    cache.get( key )
  }

}
