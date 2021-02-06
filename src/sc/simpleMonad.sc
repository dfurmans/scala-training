final class IO[A](val interpret: () => A)
object IO {
  def apply[A](a: =>A): IO[A] = new IO(() => a)
    def pure[A](a: A): IO[A] = IO(a)
    def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = {
      val part0: IO[B] = f(fa.interpret())
      val part1: () => B = part0.interpret
      IO(part1)
    }
}
