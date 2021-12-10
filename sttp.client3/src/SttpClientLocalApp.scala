import scribe.format._
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.logging.scribe.ScribeLoggingBackend
import sttp.model.HeaderNames

import java.io.File

object SttpClientLocalApp extends scala.App {

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

  okRequest()

  def okRequest() = {
    val res = emptyRequest.
      get(uri"http://localhost:8888/ok").
      send(backend)
    scribe.info(res.body.toString)
  }

}
