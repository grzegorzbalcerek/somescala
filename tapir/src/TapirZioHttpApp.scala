
import zhttp.http._
import zhttp.service.Server
import zio._
import sttp.tapir.server.ziohttp._
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._

object TapirZioHttpApp extends zio.App {
  val port = 9991
  override def run(args: List[String]) = {
    (console.putStrLn(s"Listening on port $port") *>
    Server.start(port, Routes.all)).exitCode
  }

  object Endpoints {
    val okEndpoint = endpoint.
      get.in("ok").out(stringBody)
    val systemPwdEndpoint = endpoint.
      get.in("system"/ "pwd").out(stringBody)
    val logicEndpoints = okEndpoint :: systemPwdEndpoint :: Nil
    val openapiEndpoint = SwaggerInterpreter().fromEndpoints(logicEndpoints, "title", "version")
  }


  object Logic {
    val okLogic = ZIO.succeed("ok")
    val systemPwdLogic = for {
      maybePwd <- system.env("PWD").catchAll(ex => ZIO.succeed(Some(ex.getMessage)))
      pwd = maybePwd.getOrElse("?")
    } yield pwd
  }

  object Routes {
    val systemPwdRoute = ZioHttpInterpreter().toHttp(Endpoints.systemPwdEndpoint.zServerLogic(_ => Logic.systemPwdLogic))
    val okRoute = ZioHttpInterpreter().toHttp(Endpoints.okEndpoint.zServerLogic(_ => Logic.okLogic))
    val all = okRoute +++ systemPwdRoute
  }

}

