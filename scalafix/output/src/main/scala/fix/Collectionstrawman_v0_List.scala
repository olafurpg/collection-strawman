package fix

import scala.{List => _, Nil => _, Seq => _, Vector => _, :: => _, #:: => _}
import scala.Predef.{
  Map => _,
  augmentString => _,
  intArrayOps => _,
  ArrowAssoc,
  charWrapper
}
import strawman.collection.{stringToStringOps, arrayToArrayOps}
import strawman.collection.immutable.{
  Vector,
  LazyList,
  List,
  Map,
  Nil,
  Range,
  Seq,
  ::
}
import strawman.collection.immutable.LazyList.#::

import scala.{List => _}
import strawman.collection.immutable.List
import scala.{Nil => _}
import strawman.collection.immutable.Nil
import strawman.collection.immutable.LazyList
object Collectionstrawman_v0_List {
  List(1, 2, 3)
  1 :: 2 :: 3 :: Nil
  val isEmpty: List[_] => Boolean = {
    case Nil     => true
    case x :: xs => false
  }
}
