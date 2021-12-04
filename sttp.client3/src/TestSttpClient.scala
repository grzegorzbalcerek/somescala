
import sttp.client3.circe.asJson
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{UriContext, basicRequest}
import sttp.model.StatusCode
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert}

object TestSttpClient extends DefaultRunnableSpec {
  override def spec =
  suite("test sttp")(
    test("text txt") {
      val expected =
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
      val backend = SttpBackendStub.synchronous.
        whenAnyRequest.thenRespond(Right(expected), StatusCode.Ok)
      val actual = basicRequest.get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").send(backend)
      assert(actual.code)(equalTo(StatusCode.Ok)) &&
      assert(actual.statusText)(equalTo("")) &&
      assert(actual.body)(equalTo(Right(expected)))
    },
    test("circe json") {
      import io.circe.generic.auto._
      val expected = TimeResponse(
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
      val backend = SttpBackendStub.synchronous.
        whenAnyRequest.thenRespond(Right(expected), StatusCode.Ok)
      val actual = basicRequest.get(uri"http://worldtimeapi.org/api/timezone/Europe/Warsaw.txt").
        response(asJson[TimeResponse]).
        send(backend)
      assert(actual.code)(equalTo(StatusCode.Ok)) &&
      assert(actual.body)(equalTo(Right(expected)))
    }
  )
}
