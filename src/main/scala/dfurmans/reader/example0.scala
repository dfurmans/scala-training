package dfurmans.reader

object example0 extends App {

  case class Reader[R, A](run: R => A) {
    def unit[A](a : => A): Reader[R,A] = Reader(_ => a)
    // def map[B](f: A => B) : Reader[R, B] = Reader(r=> f(run(r)))
    def map[B](f: A => B) : Reader[R, B] = Reader(run andThen f)
    // or we can write the map as
//    def map[B](f: A => B) : Reader[R, B] = Reader(f compose run)
    def flatMap[B](f: A => Reader[R, B]): Reader[R, B] = Reader(r => f(run(r)).run(r))
  }

  trait Travel
  case class SimpleTravel(from: String, to: String) extends Travel
  // upper bound type parameter - an Type A refers to
  // a subtype of Travel - here a SimpleTravel fulfill the relationship
  trait Repository[A <: Travel] {
    def save(a: A): A
  }
  trait TravelRepository extends Repository[Travel]{
    def save(t: Travel) : Travel
  }
  class TravelRepositoryArangoDB extends TravelRepository {
    def save(t: Travel): Travel = ???
  }

  trait TravelServiceMonad[X,Y]{
    def createTravelRequest(from: String, to: String): Reader[X, Y]
  }
  object TravelServiceMonadImplement extends TravelServiceMonad[TravelRepository, Travel] {
    def createTravelRequest(from: String, to : String) : Reader[TravelRepository, Travel] ={
      Reader(tr => {
        tr.save(SimpleTravel(from = from , to = to))}
      )
    }
  }
  val perform = {
  val aTripsThatNeedARepositoryStorage : Reader[TravelRepository, (Travel, Travel)] = for {
    jueves  <- TravelServiceMonadImplement.createTravelRequest("A","B")
    viernes <- TravelServiceMonadImplement.createTravelRequest("B", "A")
  } yield (jueves, viernes)
  aTripsThatNeedARepositoryStorage
  }
  println(perform)
}
