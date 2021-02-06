import cats.implicits._
import cats.{Applicative, Monad, arrow, ~>}

case class User(name : String)

val c1 = User("c1")
val c2 = User("c2")

trait Functiones[A,B] extends Serializable { self =>
  val ae : Int = 1
  def a() : Functiones[A,B] = self
}

val opse = new Functiones[Int,Int]{}

print(opse.ae)
// because this is an operator for the type constructor
// we have to pass types for it
type ::-::>[A, B] = Functiones[A, B]
// but how we can pass types for it ?
val fromOperator = new (::-::>[Int, Int]){}
val k: arrow.FunctionK[List, Set] = new (~>[List, Set]) {
  override def apply[A](fa: List[A]) : Set[A] = fa.toSet
}
val ll = new (List ~> Set){
  override def apply[A](fa: List[A]) : Set[A] = fa.toSet
}

//case class Vec(x: Double, y: Double) {
//  def +(that: Vec) = Vec(this.x + that.x, this.y + that.y)
//}
case class Vec(x: Double, y: Double) { self =>
  def c() : Boolean = this.eq(self)
  def +(that: Vec) = Vec(self.x + that.x, self.y + that.y)
}
val pp = Vec(1.0, 1.0)
val pp2 = Vec(1.0, 1.0)
println(pp.c)
println(pp + pp2)


trait Trans[F[_], G[_]]{
  def naturalTransformation[A](fa: F[A]): G[A]
}
type ::@::[F[_], G[_]] = Trans[F,G]
val setToList = new (Set ::@:: List){
  override def naturalTransformation[A](fa: Set[A]): List[A] = fa.toList
}