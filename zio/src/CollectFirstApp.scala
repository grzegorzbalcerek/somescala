
import zio._
object CollectFirstApp extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for {
      result <- ZIO.collectFirst(1 to 4)(k => getSomeInt(k, 10).
        catchAll(e => console.putStrLn(s"recovering from $e!") *> ZIO.succeed(None)))
      _ <- console.putStrLn(s"result: $result")
    } yield ()).exitCode
  }

  def getSomeInt(maxOk: Int, limit: Int) =
    for {
      x <- random.nextIntBetween(1,limit)
      _ <- console.putStrLn(s"got $x, max ok $maxOk")
      _ <- if (x == 3) {
        console.putStrLn(s"failing with ERROR3!") *> ZIO.fail("ERROR3")
      } else ZIO.succeed(())
    } yield if (x <= maxOk) Some(x) else None
}
