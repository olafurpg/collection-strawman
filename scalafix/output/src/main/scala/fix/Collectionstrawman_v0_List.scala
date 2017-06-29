package fix

import scala.{List => _, Nil => _}
import strawman.collection.immutable.{List, Nil}

object Collectionstrawman_v0_List {
  List(1, 2, 3)
  1 :: 2 :: 3 :: Nil
  val isEmpty: List[_] => Boolean = {
    case Nil     => true
    case x :: xs => false
  }
}
