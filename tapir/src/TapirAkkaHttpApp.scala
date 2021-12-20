import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._

import scala.concurrent.Future

object TapirAkkaHttpApp extends scala.App {
  val port = 9992
  println(s"Listening on port $port")
  implicit val system = ActorSystem()
  Http().newServerAt("localhost", port).bindFlow(Routes.all)

  object Endpoints {
    val okEndpoint = endpoint.
      get.in("ok").out(stringBody)
    val countEndpoint = endpoint.
      post.in("count").in(jsonBody[InputLists]).out(jsonBody[OutputCounters])
  val fullLogicEndpoints =
    Endpoints.okEndpoint.serverLogic(Logic.okLogic) ::
    Endpoints.countEndpoint.serverLogic(Logic.countLogic) ::
    Nil
    val openapiEndpoint = SwaggerInterpreter().fromServerEndpoints[Future](fullLogicEndpoints, "title", "version")
    val all = fullLogicEndpoints ++ openapiEndpoint
  }

  object Logic {
    val okLogic = (_: Unit) => Future.successful[Either[Unit, String]](Right("ok"))
    val countLogic = (inputLists: InputLists) =>
    Future.successful[Either[Unit, OutputCounters]](Right(OutputCounters(inputLists.numbers.size, inputLists.strings.size)))
  }

  object Routes {
    val all = AkkaHttpServerInterpreter().toRoute(
      Endpoints.fullLogicEndpoints ++
        Endpoints.openapiEndpoint)
  }

  case class InputLists(numbers: List[Int], strings: List[String])
  case class OutputCounters(numbers: Int, string: Int)
}

