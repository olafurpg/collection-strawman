package fix

import strawman.collection.immutable.Range

object Collectionstrawman_v0_Range {
  for (i <- Range.inclusive(1, 10); j <- Range(0, 10)) yield (i, j)
}
