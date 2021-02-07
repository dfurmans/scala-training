import cats.{
  Semigroup,
  Monoid,
  Functor ,
  Applicative,
  Monad // in that example we are using only that Type Class - there rest are enumerated only for the learn purpose
}

import cats.implicits._
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
case class DronePosition(droneName : String ,position :Coordinates)
case class DroneEntity(name: String) extends AnyVal
case class DroneConnection(name: String, attributes: Map[String, String])

// define business logic
// and abstract over effects with Higher Kinded Types
trait Drone[F[_]]{
  def getDroneByName(name: String) : F[DroneEntity]
}

trait DroneOperations[F[_]]{
  def conn(droneId: Int): F[DroneConnection]
}

trait DroneInfo[F[_]]{
  def position(droneId: Int) : F[DronePosition]
}

// Using a single capital letter name is a common naming convention for monad and algebra implementations.
// The algebra should use only another algebra on that level - in other words - do not relay on any kind
// of external resources such as IO/ Cache / DB in any kind of indirect way.
final class DroneInfoModule[F[_] : Monad](D: Drone[F],
                                          O: DroneOperations[F])
                                          //AND|OR()(implicit )
      extends DroneInfo[F]{

  def position(droneId: Int) : F[DronePosition] =  {
    for {
      aDroneConnection <- O.conn(droneId)
      aDrone           <- D.getDroneByName(aDroneConnection.name)
    } yield DronePosition(droneName = aDrone.name , Coordinates(1,2))
  }

}

// this is a place where all dependencies (for instance different interpreters will take place)
object DroneProgram {

  def whereIam[M[_]](droneId : Int)(droneModule : DroneInfo[M]) // forInstance cache API
                    (implicit M: Monad[M]) : M[DronePosition] = droneModule.position(droneId)

}