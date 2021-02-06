import cats.{Applicative, Monad, implicits}

import scala.concurrent.{ExecutionContext, Future}


case class YourlsRequest(keyword: String)
case class YourlsResponse(keyword: String, expanded: String)

trait YourlsModule {
  def yourlsService: YourlsService

  trait YourlsService {
    def resolveShortUrl(request: YourlsRequest)(implicit ec: ExecutionContext): Future[Option[YourlsResponse]]
  }

}
val myModule = new YourlsModule {
  override def yourlsService = new YourlsService {
    override def resolveShortUrl(request: YourlsRequest)(implicit ec: ExecutionContext) = ???
  }

}

// tagless final
case class Coordinates(longitude: Int, latitude: Int) // longitude W-E; latitude S-N
case class DronePosition(position :Coordinates)
case class DroneEntity(name: String) extends AnyVal
case class DroneConnection(name: String, attributes: Map[String, String])

// define business logic
// and abstract over effects with Higher Kinded Types
trait Drone[F[_]]{
  def getDroneByName(name: String) : F[DroneEntity]
}
trait DroneOperations[F[_]]{
  def conn(name: String): F[DroneConnection]
}
trait DroneInfo[F[_]]{
  def position : F[DronePosition]
}
import implicits._

// Using a single capital letter name is a common naming convention for monad and algebra implementations.
final class DroneInfoModule[F[_] : Monad](D: Drone[F], O: DroneOperations[F])
                                               //AND|OR()(implicit )
      extends DroneInfo[F]{
  def position : F[DronePosition] =  {
    val k: F[DronePosition] = for {
      con <- O.conn("")
      d   <- D.getDroneByName("")
    } yield {
      con.attributes
      d.name
      DronePosition(Coordinates(1,2))
    }
    k
  }

}

// this is a place where all dependencies (for instance different interpreters will take place
object DronProgram {
  def whereIam[M[_]](dronIntepreter :DroneInfo[M],
                     dronConnection :DroneOperations[M]) // np. cache API
                    (implicit M: Monad[M]) : M[DronePosition] ={
    M.pure(
      DronePosition(
        Coordinates(1,2)
      )
    )

  }
}