
import zio._
object ZioEnvApp {
  def main(args: Array[String]): Unit = {
    Runtime.default.unsafeRun(ZioEnv.readEnv())
  }
}

object ZioEnv {
  def readEnv() =
    for {
      pwd <- system.env("PWD")
      user <- system.env("USER")
      _ <- console.putStrLn(s"PWD: $pwd")
    } yield (pwd, user)
}
