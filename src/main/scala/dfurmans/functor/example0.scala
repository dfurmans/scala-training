package dfurmans.functor

import scala.collection.immutable.Queue

object Example0 extends App {

  object Problem0{
    type HumanName = String

    // todo :: change Queue[Human] into Queue[HumanName]
    class Human(val name: HumanName = "")

    val h0: Human = new Human("h0")
    val h1: Human = new Human("h1")

    val humanQueue: Seq[Human] = Queue[Human](h0, h1)
    val fromHumanToHumanNameFunction: Human => HumanName = humanName => humanName.name
    val humanName: Seq[HumanName] = FunctorDefinition.ourFunctorForSeq.map(humanQueue)(fromHumanToHumanNameFunction)

  }

  lazy val toPerformerDefinition: String = {
    s"""
       |Functor Example ::
       | => FunctorRules.identityRule -> ${FunctorDefinition.FunctorRules.identityRule}
       |
       |""".stripMargin.stripLineEnd
  }

  println(toPerformerDefinition)

  object FunctorDefinition {

    // our functor definition declaration
    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B): F[B]
    }

    // todo :: explain it
    object FunctorRules{
      val identityRule = ourFunctorForSeq.map(Seq.empty[Int])(x => x) == Seq.empty[Int]
    }

    // a simple functor baked/based on Seq implementation
    val ourFunctorForSeq: Functor[Seq] = new Functor[Seq] {
      override def map[A, B](fa: Seq[A])(f: A => B): Seq[B] = fa.map(f)
    }
  }

}