package fix

import scala.meta._
import scalafix.testkit._
import org.scalameta.logger

class Collectionstrawman_Tests
  extends SemanticRewriteSuite(
    Database.load(Classpath(AbsolutePath(BuildInfo.inputClassdirectory))),
    AbsolutePath(BuildInfo.inputSourceroot),
    Seq(AbsolutePath(BuildInfo.outputSourceroot))
  ) {
  override def assertNoDiff(obtained: String, expected: String, title: String): Boolean = {
    logger.elem(obtained)
    super.assertNoDiff(obtained, expected, title)
  }
  runAllTests()
}
