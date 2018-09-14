package foxhound.infrastructure.module

import akka.Done
import akka.actor.{ ActorSystem, Cancellable, Scheduler }
import com.google.inject.AbstractModule
import foxhound.domain.Message
import javax.inject.{ Inject, Singleton }
import play.api.{ Configuration, Logger }
import play.api.cache.{ AsyncCacheApi, NamedCache }
import play.api.inject.ApplicationLifecycle
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import dispatch._
import akka.actor._

trait Loader {
  val actorSystem: ActorSystem
}

@Singleton
class CacheLoader @Inject() (
  @NamedCache( "foxhound-cache" ) cache:AsyncCacheApi,
  val actorSystem:                     ActorSystem,
  config:                              Configuration,
  lifecycle:                           ApplicationLifecycle
) extends Loader {

  val msgs = List(
    Message( 1, "141-52-001", "Nastasha", "This is Nastasha Romanenko. A pleasure to work with you Solid Snake." ),
    Message( 2, "141-52-002", "Snake", "You're the nuclear specialist that the Colonel mentioned?" ),
    Message( 3, "141-52-003", "Nastasha", "That's me. You can ask me anything about nukes that you want. I am also a military analyst, so I have an extensive knowledge of weapons systems as well. They asked me to participate in this operation as a supervisor from the Nuclear Emergency Search Team. I was happy to accept. We must not allow terrorists to get their hands on nuclear weapons on any kind. I hope I can help you to stop them." ),
    Message( 4, "141-52-004", "Snake", "You're a tough lady." ),
    Message( 5, "141-52-005", "Nastasha", "Those terrorists are serious about launching a nuclear weapon. The world cannot stand by idly and allow that to happen. And neither can I... Unfortunately, all I can do from here is provide you with information..." ),
    Message( 6, "141-52-006", "Snake", "Hopefully that'll be enough. Another soldier here wouldn't make a difference anyway. It's good to work with you, Nastasha." ),
    Message( 7, "141-52-007", "Nastasha", "Same here, Snake." )
  )

  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher
  Logger.warn( "Actor System started." )

  val duration: FiniteDuration = FiniteDuration( 5, SECONDS )
  val scheduler: Scheduler = actorSystem.scheduler

  val carga: () => Future[Done] = () => cache.set( "messages", msgs )

  val schedule: Cancellable = scheduler.schedule( 10.seconds, duration ) {
    Logger.warn( "Refreshing cache..." )
    cache.set( "messages", msgs )
    //TODO. retry.Backoff
  }

  lifecycle.addStopHook { () =>
    Logger.warn( "Stoping actor system..." )
    actorSystem.terminate().map( status => Logger.warn( s"Actor system terminated" ) )
  }

}

class CacheModule extends AbstractModule {

  override def configure(): Unit = {
    bind( classOf[Loader] ).to( classOf[CacheLoader] ).asEagerSingleton()
  }

}