
import io.circe.{Decoder, Encoder}
import sttp.client3.circe._
import sttp.client3.testing.{RecordingSttpBackend, SttpBackendStub}
import sttp.client3.{UriContext, basicRequest, emptyRequest}
import sttp.model.StatusCode
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert}

object TestSttpClient extends DefaultRunnableSpec {
  val expectedTimeResponseJson = TimeResponse(
    abbreviation = "CET",
    client_ip = "37.248.209.230",
    datetime = "2021-12-03T15:56:38.276412+01:00",
    day_of_week = 5,
    day_of_year = 337,
    dst = false,
    dst_from = None,
    dst_offset = 0,
    dst_until = None,
    raw_offset = 3600,
    timezone = "Europe/Warsaw",
    unixtime = 1638543398,
    utc_datetime = "2021-12-03T14:56:38.276412+00:00",
    utc_offset = "+01:00",
    week_number = 48)
  val expectedTimeResponseTxt =
    """abbreviation: CET
      |client_ip: 37.248.209.230
      |datetime: 2021-12-03T15:56:38.276412+01:00
      |day_of_week: 5
      |day_of_year: 337
      |dst: false
      |dst_from:
      |dst_offset: 0
      |dst_until:
      |raw_offset: 3600
      |timezone: Europe/Warsaw
      |unixtime: 1638543398
      |utc_datetime: 2021-12-03T14:56:38.276412+00:00
      |utc_offset: +01:00
      |week_number: 48""".stripMargin
  override def spec =
  suite("test sttp")(
    test("recording") {
      val backend = SttpBackendStub.synchronous.
        whenRequestMatches(r => r.uri.authority.map(_.host) == Some("worldtimeapi.org")).
        thenRespond(expectedTimeResponseTxt, StatusCode.Ok).
        whenRequestMatches(r => r.uri.authority.map(_.host) == Some("worldtimeapi.org")).
        thenRespond(expectedTimeResponseJson, StatusCode.Ok)
      val recordingBackend = new RecordingSttpBackend(backend)
      val actual1 = basicRequest.get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").send(recordingBackend)
      val actual2 = emptyRequest.get(uri"http://worldtimeapi.org/api/timezone/Europe/Rome").send(recordingBackend)
      assert(recordingBackend.allInteractions.length)(equalTo(2)) &&
      assert(recordingBackend.allInteractions.head._1.uri.scheme)(equalTo(Some("http"))) &&
      assert(recordingBackend.allInteractions(0)._1.uri.authority.map(_.host))(equalTo(Some("worldtimeapi.org"))) &&
      assert(recordingBackend.allInteractions(0)._1.uri.path)(equalTo(List("api", "timezone", "Europe", "Warsaw.txt"))) &&
      assert(recordingBackend.allInteractions(1)._1.uri.path)(equalTo(List("api", "timezone", "Europe", "Rome"))) &&
      assert(recordingBackend.allInteractions(1)._1.headers)(equalTo(Seq())) &&
      assert(actual1.code)(equalTo(StatusCode.Ok)) &&
      assert(actual1.statusText)(equalTo("")) &&
      assert(actual1.body)(equalTo(Right(expectedTimeResponseTxt)))
    },
    test("text txt getRight") {
      val backend = SttpBackendStub.synchronous.
        whenAnyRequest.thenRespond(expectedTimeResponseTxt, StatusCode.Ok)
      val actual = basicRequest.get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").responseGetRight.send(backend)
      assert(actual.code)(equalTo(StatusCode.Ok)) &&
      assert(actual.statusText)(equalTo("")) &&
      assert(actual.body)(equalTo(expectedTimeResponseTxt))
    },
    test("circe json") {
      import io.circe.generic.semiauto._
      implicit val encoder: Encoder[InputLists] = deriveEncoder[InputLists]
      implicit val decoder: Decoder[TimeResponse] = deriveDecoder[TimeResponse]
      val backend = SttpBackendStub.synchronous.
        whenAnyRequest.thenRespond(Right(expectedTimeResponseJson), StatusCode.Ok)
      val recordingBackend = new RecordingSttpBackend(backend)
      val actual = basicRequest.put(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").
        body(InputLists(List(1),List(""))).
        response(asJson[TimeResponse]).
        send(recordingBackend)
      assert(actual.code)(equalTo(StatusCode.Ok)) &&
      assert(actual.body)(equalTo(Right(expectedTimeResponseJson)))
    }
  )
}
