package dfurmans.semigroup

import cats.Semigroup

object example0 extends App {

  import CombineItWithSemigroup.Usage._

  val toPerformerDefinition = {
    s"""
       |Semigroup Example ::
       | => Semigroup operator and def should yield the same result -> ${combineWithDef == combineWithOperator}
       |
       |""".stripMargin.stripLineEnd
  }
  print(toPerformerDefinition)

  object CombineItWithSemigroup {

    case class Profit(aProfitValue: Int)

    implicit val aSemigroupsForProfit: Semigroup[Profit] = new Semigroup[Profit] {
      // the semigroup type class does not provide an empty element (we need more power from the Monoid)
      override def combine(x: Profit, y: Profit): Profit = Profit(x.aProfitValue + y.aProfitValue)
    }

    object Usage {
      val p1 = Profit(1)
      val p2 = Profit(1)
      val combineWithDef: Profit = aSemigroupsForProfit.combine(p1, p2)
      // OR

      import cats.implicits._

      val combineWithOperator: Profit = p1 |+| p2

    }

  }

}
