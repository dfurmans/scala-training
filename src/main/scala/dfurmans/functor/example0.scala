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

  val toPerformerDefinition = {
    s"""
       |Functor Example ::
       | => FunctorRules.identityRule -> ${FunctorDefinition.FunctorRules.identityRule}
       |
       |""".stripMargin.stripLineEnd
  }
  print(toPerformerDefinition)

  object FunctorDefinition {

    // our functor definition declaration
    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B): F[B]
      def lift[A, B](f: A => B): F[A] => F[B]
    }

    // todo theory :: explain it
    object FunctorRules{
      val identityRule = ourFunctorForSeq.map(Seq.empty[Int])(x => x) == Seq.empty[Int]
    }

    // a simple functor baked/based on Seq implementation
    val ourFunctorForSeq: Functor[Seq] = new Functor[Seq] {
      override def map[A, B](fa: Seq[A])(f: A => B): Seq[B] = fa.map(f)

      // TODO practice
      override def lift[A, B](f: A => B): Seq[A] => Seq[B] = ???
    }
  }

  object Extra{
    trait Functors[F[_]]{
      def void[A](fa: F[A]): F[Unit] = ???
      def fproduct[A, B](fa: F[A])(f: A => B): F[(A, B)] = ???
      def as[A, B](fa: F[A], b: B): F[B] = ???
      def tupleLeft[A, B](fa: F[A], b: B): F[(B, A)] = ???
      def tupleRight[A, B](fa: F[A], b: B): F[(A, B)] = ???
      def unzip[A, B](fab: F[(A, B)]): (F[A], F[B]) = ???
      // harder
      def lift[A, B](f: A => B): F[A] => F[B] = ???
    }

  }
}