package dfurmans {
  package composition {

    object Example0 extends App {

      object example0 {
        val f1: String => Int = ???
        val f2: Int => Int = ???
        val f2Composef1: String => Int = f2 compose f1
        val f1AndThenf2: String => Int = f1 andThen f2
        val simpleCompo: String => Int = f2.compose(f1)

        // or as a standard method
        def f11(x: String): Int = ???
        def f22(x: Int): Int = ???
        val f22Composef11: String => Int = f22 _ compose f11 _
      }
      lazy val toEvaluate = {
        ???
      }
      println(toEvaluate)
    }

  }

}
