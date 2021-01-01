package info.maalvarez.lesson01

trait Math[A] {
  def sum(x: A, y: A): A
  def sum(x: Tuple[A]): A
  def operate(operation: (A, A) => A, x: A, y: A): A
}

// As a ad-hoc Polymorphism
object Math{
  implicit object MathLikeForInt extends Math[Int] {
    def sum(x: Int, y: Int): Int = x + y
    def sum(x: Tuple[Int]): Int = sum(x.first, x.second)
    def operate(operation: (Int, Int) => Int, x: Int, y: Int): Int = operation(x, y)
  }
  // Easy add MathLike behaviour for another type T as an simple
  // extension Math[Int]
}
