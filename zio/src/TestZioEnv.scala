import zio.test.TestAspect.{jvmOnly, silent}
import zio.test.Assertion.{equalTo, isLessThan, isSome}
import zio.test.environment.{TestConsole, TestSystem}
import zio.test.{DefaultRunnableSpec, assert}

object TestZioEnv extends DefaultRunnableSpec {
  override def spec =
    suite("ReadEnv test suite")(
      test("simple test") {
        assert(1 + 3)(equalTo(4)) && assert(3)(isLessThan(5))
      } @@ jvmOnly @@ silent,
      testM("check ReadEnv") {
        for {
          _ <- TestSystem.putEnv("PWD", "/tmp")
          _ <- TestSystem.putEnv("USER", "test")
          result <- ZioEnv.readEnv()
          out <- TestConsole.output
        } yield assert(result._1)(isSome(equalTo("/tmp"))) &&
          assert(result._2)(isSome(equalTo("test"))) &&
          assert(out)(equalTo(Vector("PWD: Some(/tmp)\n")))
      }
    ) @@ silent
}
