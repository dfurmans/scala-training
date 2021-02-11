package dfurmans.state

import cats.effect.{ExitCode, IO, IOApp}

// represents a State change
// the method run is taking a state and returning a new state with a Value A
case class State[S, A](run: S => (S,A)) {
  def map[B](f: A => B) : State[S,B] = {
    State{ aState =>
      val (newState, data) = run(aState)
      val aB: B = f(data)
      (newState, aB)
    }
  }
  def flatMap[B](f: A => State[S,B]) : State[S,B] = State{s =>
    val (s1, a) = run(s)
    val (s2,b) = f(a).run(s1)
    (s2,b)
  }
}

object example0 extends IOApp{

  def increase(shootPoints: Int) : State[Int, Int] = State{ s =>
    (s, s + shootPoints)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    IO{
      val k: State[Int, (Int, Int, Int)] = for {
        s1  <- increase(1)
        s2 <- increase(2)
        s3 <- increase(3)
      } yield (s1,s2,s3)
      print(k.run(0))
      ExitCode.Success
    }
  }
}
