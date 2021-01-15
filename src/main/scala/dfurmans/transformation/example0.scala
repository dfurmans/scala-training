package dfurmans.transformation

object example0 extends App{

  object HigherKindedTypeTransformation {

    case class User(name: String)

    val c1 = User("c1")
    val c2 = User("c2")

    trait Trans[F[_], G[_]] {
      def naturalTransformation[A](fa: F[A]): G[A]
    }
    type ::@::[F[_], G[_]] = Trans[F, G]
    val setToList = new (Set ::@:: List) {
      override def naturalTransformation[A](fa: Set[A]): List[A] = fa.toList
    }

    object byCats{
      import cats.~>
      val listToSetByCats = new (List ~> Set) {
        override def apply[A](fa: List[A]): Set[A] = fa.toSet
      }
      listToSetByCats(List(c1,c2))
    }
  }
  val toPerformerDefinition = {

    import HigherKindedTypeTransformation.byCats.listToSetByCats
    import HigherKindedTypeTransformation.{User, c1, c2, setToList}

    val fromInit: Set[User] = Set(c1, c2)
    val to: List[User] = setToList.naturalTransformation(fromInit)
    val from: Set[User] = listToSetByCats(to)
    s"""
       |Natural Transformation Example ::
       | => Set to List to Set should be the same ${fromInit == from}
       |
       |""".stripMargin.stripLineEnd
  }

  print(toPerformerDefinition)
}
