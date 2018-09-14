package foxhound.domain.service

import cats.syntax.either._
import cats.data.{ EitherT, Reader }
import foxhound.domain.Message
import foxhound.domain.repository.MessageRepo
import foxhound.domain.service.MessageService.RepoReader

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait MessageService {

  type RepoReader[A] = Reader[MessageRepo, EitherT[Future, String, A]]

  def listMessages(): RepoReader[List[Message]]

  def findMessage( id: Long ): RepoReader[Option[Message]]

}

object MessageService extends MessageService {

  def listMessages(): RepoReader[List[Message]] = Reader {
    repo: MessageRepo =>
      {
        val result = repo.list()
          .map { msgs => msgs.asRight[String] }
          .recoverWith {
            case ex: Exception =>
              Future.successful( ex.getMessage.asLeft[List[Message]] )
          }
        EitherT( result )
      }
  }

  def findMessage( id: Long ): RepoReader[Option[Message]] = Reader {
    repo: MessageRepo =>
      {
        val result = repo.find( id )
          .map { msg => msg.asRight[String] }
          .recoverWith {
            case ex: Exception =>
              Future.successful( ex.getMessage.asLeft[Option[Message]] )
          }
        EitherT( result )
      }
  }

}