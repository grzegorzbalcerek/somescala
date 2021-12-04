
import scribe.file._
import scribe.format._
import scribe.output.format.ASCIIOutputFormat

object ScribeApp extends scala.App {

  doScribe()
  setupFormatter()
  doScribe()
  setupFileWriter()
  doScribe()

  def setupFormatter() = {
    scribe.Logger.root.
      clearHandlers().
      clearModifiers().
      withHandler(
        minimumLevel = Some(scribe.Level.Debug),
        formatter = formatter"$dateFull [$threadName] [${levelColor(level)}] $fileName:$line $methodName - $message").
      replace()
  }

  def setupFileWriter() = {
    scribe.Logger.root.
      clearHandlers().
      clearModifiers().
      withHandler(
        formatter = Formatter.classic,
        outputFormat = ASCIIOutputFormat,
        writer = FileWriter("app.log", append = false)).
      replace()
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