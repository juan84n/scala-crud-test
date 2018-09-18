package accounting.infrastructure.module

import accounting.domain.repository.{ AccountRepoInMemory, AccountRepository }
import com.google.inject.AbstractModule
import play.api.Logger

trait Greeting {
  def sayHello( name: String ): String
}

class SpanishGreeting() extends Greeting {
  def sayHello( name: String ) = s"Hola $name"
}

class EnglishGreeting() extends Greeting {
  def sayHello( name: String ) = s"Hello $name"
}

class BinderModule extends AbstractModule {

  override def configure(): Unit = {

    Logger.info( "BinderModule... (ok)" )

    bind( classOf[Greeting] ).to( classOf[SpanishGreeting] ).asEagerSingleton()
    bind( classOf[AccountRepository] ).to( classOf[AccountRepoInMemory] ).asEagerSingleton()

  }

}