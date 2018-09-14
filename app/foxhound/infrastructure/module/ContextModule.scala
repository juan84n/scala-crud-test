package foxhound.infrastructure.module

import com.google.inject.AbstractModule
import foxhound.domain.repository.MessageRepo
import foxhound.infrastructure.persistence.repository.{ MessageCacheRepo, MessageDbRepo, MessageH2Repo }

class ContextModule extends AbstractModule {

  override def configure(): Unit = {
    //bind( classOf[MessageRepo] ).to( classOf[MessageDbRepo] ).asEagerSingleton()
    //bind( classOf[MessageRepo] ).to( classOf[MessageH2Repo] ).asEagerSingleton()
    bind( classOf[MessageRepo] ).to( classOf[MessageCacheRepo] ).asEagerSingleton()
  }

}