package fix

import scala.Predef.{ intArrayOps => _ }
import strawman.collection.arrayToArrayOps
object Collectionstrawman_v0_ArrayAndString {
  def foo(xs: Array[Int], ys: String): Unit = {
    xs.map(x => x + 1)
    ys.map(c => c.toUpper)
  }
}
