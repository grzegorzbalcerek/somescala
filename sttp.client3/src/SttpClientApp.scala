import scribe.format._
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.logging.scribe.ScribeLoggingBackend
import sttp.model.HeaderNames

import java.io.File

object SttpClientApp extends scala.App {

  val backend = ScribeLoggingBackend(
    delegate = HttpURLConnectionBackend(),
    sensitiveHeaders = HeaderNames.SensitiveHeaders ++ Set("server")
  )

  scribe.Logger("sttp.client3.logging.scribe.ScribeLogger").
    clearModifiers().
    clearHandlers().
    withHandler(
      minimumLevel = Some(scribe.Level.Debug),
      formatter = formatter"$dateFull [$threadName] [${levelColor(level)}] $className:$line - $message").
    replace()

  txtRequest()
  textJsonRequest()
  circeJsonRequest()
  okRequest()

  def txtRequest() = {
    val txt = emptyRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").
      cookie("mycookie","value").
      header("myheader","somevalue").
      response(asFile(new File("res.txt"))).
      send(backend)
    scribe.info(txt.body.toString)
  }

  def textJsonRequest() = {
    val json = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw").
      send(backend)
    scribe.info(json.body.toString)
  }

  def circeJsonRequest() = {
    import io.circe.generic.auto._
    val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw").
      response(asJson[TimeResponse]).
      send(backend)
    scribe.info(res.body.toString)
  }

  def okRequest() = {
    val res = emptyRequest.
      get(uri"http://localhost:8888/ok").
      send(backend)
    scribe.info(res.body.toString)
  }

}
