package info.maalvarez.lesson01

object Main {
  def main(args: Array[String]): Unit = {
    printMessage("Good morning!")

    val aResult: Int = doMathOperationForGenericType(1,2)
    println("A doMathOperationForGenericType result " + aResult)
    val a: MathInt = new MathInt()
    a.factorial(10)
  }

  // Do the math operation for a generic type T
  def doMathOperationForGenericType[T] (x: T, y : T)(implicit  mathLike: Math[T])={
    mathLike.sum(x, y)
  }

  def printMessage(s: String) = println("Good morning!")
}