
import scribe._
import scribe.file._
import scribe.format._
import scribe.output.format.ASCIIOutputFormat

object ScribeFilesApp extends scala.App {

  setup()
  scribe.info("scribe")
  scribe.Logger("app").info("app")
  "log".logger.info("log")

  def setup() = {
    scribe.Logger.root.
      clearHandlers().
      clearModifiers().
      withHandler(
        minimumLevel = Some(scribe.Level.Debug),
        formatter = formatter"$dateFull [$threadName] [${levelColor(level)}] $fileName:$line $methodName - $message").
      replace()
    scribe.Logger("app").
      clearHandlers().
      clearModifiers().
      orphan().
      withHandler(
        formatter = Formatter.classic,
        outputFormat = ASCIIOutputFormat,
        writer = FileWriter("app.log", append = false)).
      replace()
    scribe.Logger("log").
      clearHandlers().
      clearModifiers().
      withHandler(
        formatter = Formatter.classic,
        outputFormat = ASCIIOutputFormat,
        writer = FileWriter("log.log", append = false)).
      replace()
  }

  def setupFileWriter() = {
  }

  def doScribe() = {
    scribe.trace("trace message")
    scribe.debug("debug message")
    scribe.error("error message")
    scribe.info("info message")
    scribe.warn("warn message")
    scribe.info(scribe.Logger.root.handlers.toString)
    scribe.info(scribe.Logger.root.modifiers.toString)
  }
}