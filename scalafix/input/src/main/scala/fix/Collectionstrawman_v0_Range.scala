/* ONLY
rewrite = "scala:fix.Collectionstrawman_v0"
 */
package fix

object Collectionstrawman_v0_Range {
  for (i <- 1 to 10; j <- 0 until 10) yield (i, j)
}
