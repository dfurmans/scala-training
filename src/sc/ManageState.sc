// working with state
// create a desired state as a case class
// writing a methods that takes a current state and
// change the state base on the values
case class B[-T,+Y](run : T => Y)

sealed trait GameState
object GameState {

  case object WrongGameState extends GameState

  case class BowlingState(pins: Int = BowlingState.defaultToWin,
                          strokeTracking: List[Int] = List(BowlingState.defaultToWin)
                         ) extends GameState
  object BowlingState{
    val defaultToWin = 10
    def nextStroke(state: BowlingState,
                   pinsHit: Int
                  ) : GameState = {
      val resultAtThisStroke = state.pins - pinsHit
      if (resultAtThisStroke<0) WrongGameState
      else BowlingState(
        resultAtThisStroke,
        state.strokeTracking:+resultAtThisStroke
      )
    }
  }
}


// type write as BowlingState
// constructor write as BowlingState()
val round0 = GameState.BowlingState()
val round1 = GameState.BowlingState.nextStroke(round0, 4)
println(round1)

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

def play(shootPoints: Int) : State[GameState.BowlingState, Int] = State{ s =>
  val aSingleRoll = s.pins - shootPoints
  (GameState.BowlingState(aSingleRoll), aSingleRoll)
}

val k: State[GameState.BowlingState, Int] = for {
  _  <- play(2)
  r2 <- play(10)
} yield r2

// we have to pass an inicial state to our game rules
val start = GameState.BowlingState()
val result: (GameState.BowlingState, Int) = k.run(start)
println(s"The Status game: ${result._1} , with the final shoot points ${result._2}")
