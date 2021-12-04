import zhttp.http._
import zhttp.service.Server
import zio._
object ZioHttpApp extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val port = 9990
    (console.putStrLn(s"Listening on port $port") *> Server.start(port, Routes.all)).exitCode
  }

  object Routes {
    val systemPwdRoute = HttpApp.collectM {
      case Method.GET -> Root / "system" / "pwd" => for {
        pwd <- system.env("PWD")
      } yield Response.text(pwd.getOrElse("?"))
    }
    val okRoute = HttpApp.collect {
      case Method.GET -> Root / "ok" =>
        Response.text("ok")
    }
    val all = systemPwdRoute +++ okRoute
  }

}
