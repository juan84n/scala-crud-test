/*package accounting.infrastructure.module

import accounting.infrastructure.persistence.repository.CacheRepository
import akka.Done
import com.google.inject.AbstractModule
import javax.inject.Inject
import play.api.Logger
import scala.concurrent.Future

trait Loader {

}

class CacheLoader @Inject() ( cacheRepo: CacheRepository ) extends Loader {
  cacheRepo.upload()
}

class CacheModule extends AbstractModule {

  override def configure(): Unit = {
    Logger.info( "CacheModule... (ok)" )
    bind( classOf[Loader] ).to( classOf[CacheLoader] ).asEagerSingleton()
  }

}*/ 