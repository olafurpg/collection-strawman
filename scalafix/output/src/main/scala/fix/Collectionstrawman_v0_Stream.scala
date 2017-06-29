package fix

import strawman.collection.immutable.LazyList
import strawman.collection.immutable.LazyList.#::

object Collectionstrawman_v0_Stream {
  LazyList(1, 2, 3)
  1 #:: 2 #:: 3 #:: LazyList.Empty
  val isEmpty: LazyList[_] => Boolean = {
    case LazyList.Empty => true
    case x #:: xs     => false
  }
}
