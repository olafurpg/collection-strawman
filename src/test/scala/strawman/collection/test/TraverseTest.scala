package strawman

package collection.test

import org.junit.Test
import strawman.collection._
import strawman.collection.mutable.{ArrayBuffer, Builder, Growable}

import scala.{Any, Either, Int, Left, None, Option, Right, Some, Unit}
import java.lang.String
import scala.Predef.ArrowAssoc
import scala.math.Ordering

class TraverseTest {

  // You can either overload methods for IterableOps and Iterable with SortedOps (if you want to support constrained collection types)
  def optionSequence1[C[X] <: IterableOps[X, C, _], A](xs: C[Option[A]]): Option[C[A]] =
    xs.foldLeft[Option[Builder[A, C[A]]]](Some(Builder.from[A, C](xs.iterableFactory))) {
      case (Some(builder), Some(a)) => Some(builder += a)
      case _ => None
    }.map(_.result)
  def optionSequence1[C[X] <: Iterable[X] with SortedOps[X, C[X], C], A : Ordering](xs: C[Option[A]]): Option[C[A]] =
    xs.foldLeft[Option[Builder[A, C[A]]]](Some(Builder.from[A, C](xs.sortedIterableFactory))) {
      case (Some(builder), Some(a)) => Some(builder += a)
      case _ => None
    }.map(_.result)

  // ...or use BuildFrom to abstract over both and also allow building arbitrary collection types
  def optionSequence2[CC[X] <: Iterable[X], A, To](xs: CC[Option[A]])(implicit bf: BuildFrom[CC[Option[A]], A, To]): Option[To] =
    xs.foldLeft[Option[Builder[A, To]]](Some(Builder.from(bf, xs))) {
      case (Some(builder), Some(a)) => Some(builder += a)
      case _ => None
    }.map(_.result)

  @Test
  def optionSequence1Test: Unit = {
    val xs1 = immutable.List(Some(1), None, Some(2))
    val o1 = optionSequence1(xs1)
    val o1t: Option[immutable.List[Int]] = o1

    val xs2: immutable.TreeSet[Option[String]] = immutable.TreeSet(Some("foo"), Some("bar"), None)
    val o2 = optionSequence1(xs2)
    val o2t: Option[immutable.Set[String]] = o2

    val xs4 = immutable.List[Option[(Int, String)]](Some((1 -> "a")), Some((2 -> "b")))
    val o4 = optionSequence1(xs4)
    val o4t: Option[immutable.List[(Int, String)]] = o4
    val o5: Option[immutable.TreeMap[Int, String]] = o4.map(_.to(immutable.TreeMap))
  }

  def optionSequence2Test: Unit = {
    val xs1 = immutable.List(Some(1), None, Some(2))
    val o1 = optionSequence2(xs1)
    val o1t: Option[immutable.List[Int]] = o1

    val xs2 = immutable.TreeSet(Some("foo"), Some("bar"), None)
    val o2 = optionSequence2(xs2)
    val o2t: Option[immutable.TreeSet[String]] = o2

    // Breakout-like use case from https://github.com/scala/scala/pull/5233:
    val xs4 = immutable.List[Option[(Int, String)]](Some((1 -> "a")), Some((2 -> "b")))
    val o4 = optionSequence2(xs4)(immutable.TreeMap) // same syntax as in `.to`
    val o4t: Option[immutable.TreeMap[Int, String]] = o4
  }

  /*

def eitherSequence[C[X] <: Iterable[X], A, B](xs: C[Either[A, B]])(implicit bf: BuildFrom[xs.type, B]): Either[A, bf.To] =
  xs.foldLeft[Either[A, Builder[B, bf.To]]](Right(bf.newBuilder(xs))) {
    case (Right(builder), Right(b)) => Right(builder += b)
    case (Left(a)       ,        _) => Left(a)
    case (_             ,  Left(a)) => Left(a)
  }.map(_.result)

def optionSequenceTest: Unit = {
  val xs1 = immutable.List(Some(1), None, Some(2))
  val o1 = optionSequence(xs1)
  val o1t: Option[immutable.List[Int]] = o1

  val xs2 = immutable.TreeSet(Some("foo"), Some("bar"), None)
  val o2 = optionSequence(xs2)
  val o2t: Option[immutable.TreeSet[String]] = o2

  // Breakout-like use case from https://github.com/scala/scala/pull/5233:
  val xs4 = immutable.List[Option[(Int, String)]](Some((1 -> "a")), Some((2 -> "b")))
  val o4 = optionSequence(xs4)(immutable.TreeMap) // same syntax as in `.to`
  val o4t: Option[immutable.TreeMap[Int, String]] = o4
}

def eitherSequenceTest: Unit = {
  val xs3 = mutable.ListBuffer(Right("foo"), Left(0), Right("bar"))
  val e1 = eitherSequence(xs3)
  val e1t: Either[Int, mutable.ListBuffer[String]] = e1
}
*/

}
