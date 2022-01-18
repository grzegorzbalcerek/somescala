import io.circe.Decoder
import scribe.format._
import sttp.client3.SttpClientException.ReadException
import sttp.client3._
import sttp.client3.circe._
import sttp.client3.logging.scribe.ScribeLoggingBackend
import sttp.model.HeaderNames

import java.io.File

object SttpClientTimeApp extends scala.App {

  val basicBackend = HttpURLConnectionBackend()
  val loggingBackend = ScribeLoggingBackend(
    delegate = basicBackend,
    sensitiveHeaders = HeaderNames.SensitiveHeaders ++ Set("server")
  )

  scribe.Logger("sttp.client3.logging.scribe.ScribeLogger").
    clearModifiers().
    clearHandlers().
    withHandler(
      minimumLevel = Some(scribe.Level.Info),
      formatter = formatter"$dateFull [$threadName] [${levelColor(level)}] $className:$line - $message").
    replace()

  txt()
  basic()
  asStringGetRight
  circeAsJson()
  circeAsJsonRetRight()
  wrongCirceAsJson()
  wrongCirceAsJsonRetRight()
  wrongAsJsonEither()

  def txt() = {
    val resp = emptyRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").
      cookie("mycookie","value").
      header("myheader","somevalue").
      response(asFile(new File("res.txt"))).
      send(loggingBackend)
    scribe.info(resp.body.toString)
  }

  def basic() = {
    val resp = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Paris").
      send(loggingBackend)
    scribe.info(resp.body.toString)
  }

  def asStringGetRight() = {
    val json = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Berlin").
      response(asString.getRight).
      send(loggingBackend)
    scribe.info(json.body.toString)
  }

  def circeAsJson() = {
    import io.circe.generic.auto._
    val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Rome").
      response(asJson[TimeResponse]).
      send(loggingBackend)
    scribe.info(res.body.toString)
  }

  def circeAsJsonRetRight() = {
    import io.circe.generic.semiauto._
    implicit val decoder: Decoder[TimeResponse] = deriveDecoder[TimeResponse]
    val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Sofia").
      response(asJson[TimeResponse].getRight).
      send(loggingBackend)
    scribe.info(res.body.toString)
  }

  def wrongCirceAsJson() = {
    import io.circe.generic.auto._
    val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/Prague").
      response(asJson[InputLists]).
      send(loggingBackend)
    scribe.info(res.body.toString)
  }

  def wrongCirceAsJsonRetRight() = {
    import io.circe.generic.auto._
    try {
      val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/MadridXXXX").
      response(asJson[InputLists].getRight).
      send(basicBackend)
      scribe.info(res.body.toString)
    } catch {
      case ex: ReadException =>
        ex.getCause match {
          case HttpError(body, statusCode) =>
            scribe.error("HttpError: "+statusCode+"; "+body.toString)
          case _ =>
            scribe.error("some error: "+ex.getCause.toString)
        }
    }
  }

  def wrongAsJsonEither() = {
    import io.circe.generic.auto._
    try {
      val res = basicRequest.
      get(uri"http://worldtimeapi.org/api/timezone/Europe/MadridXXXX").
      response(asJsonEither[ErrorResp, InputLists].getRight).
      send(basicBackend)
      scribe.info(res.body.toString)
    } catch {
      case ex: ReadException =>
        ex.getCause match {
          case HttpError(body, statusCode) =>
            scribe.error("HttpError: "+statusCode+"; "+body.getClass+"; "+body.toString)
          case _ =>
            scribe.error("some error: "+ex.getCause.toString)
        }
    }
  }

}
