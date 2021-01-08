package dfurmans.monoid

import cats.kernel.Monoid
import cats.implicits._

object example0 extends App {

  val toPerformerDefinition = {
    import NothingIsSomething._
    val i1 = Interval(2)
    val i2 = Interval(2)
    val iResult: Interval = i1.combine(i2)
    s"""
       |Monoid Example ::
       | => Combine two Monoids -> $iResult
       |
       |""".stripMargin.stripLineEnd
  }
  print(toPerformerDefinition)

  object NothingIsSomething {

    case class Interval(halftones: Int = 0)

    implicit val intervalMonoid: Monoid[Interval] = Monoid.instance(
      emptyValue = Interval(),
      cmb = (i1, i2) => Interval(i1.halftones + i2.halftones)
    )

  }

}