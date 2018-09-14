package accounting.application.context

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object context {
  val executionContext: ExecutionContext = ExecutionContext.fromExecutorService( Executors.newFixedThreadPool( 15 ) )
}