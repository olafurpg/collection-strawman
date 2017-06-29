package fix

import scalafix._, syntax._
import scala.meta._
import org.scalameta.logger

case class Collectionstrawman_v0(mirror: Database)
    extends SemanticRewrite(mirror) {

  def forCtx(ctx: RewriteCtx) =
    Database(
      mirror.entries.filter(_._1.label == ctx.tree.tokens.head.input.label))
  def rewrite(ctx: RewriteCtx): Patch = {
    logger.elem(forCtx(ctx))
    def immutable(name: String, pkg: Boolean = true) = {
      val rename = Name.Indeterminate(name)
      ctx.replace(
        Symbol(s"_root_.scala.collection.immutable.$name."),
        Term.Name(name),
        additionalImports = List(
          importer"scala.{$rename => _}",
          importer"strawman.collection.immutable.$rename"
        )
      ) + {
        if (!pkg) Patch.empty
        else
          ctx.replace(
            Symbol(s"_root_.scala.package.$name."),
            Term.Name(name),
            additionalImports = List(
              importer"scala.{$rename => _}",
              importer"strawman.collection.immutable.$rename"
            )
          )
      }
    }
    ctx.tree.traverse {
      case name @ Term.Name("#::") =>
        logger.elem(name, name.symbol)
    }
    val names = mirror.entries.head._2.copy(denotations = Nil, sugars = Nil)
    // TODO(olafur) add support to remove import by symbol
    //      ctx.removeImportee(importee"scala.collection.immutable.HashMap") +
    immutable("List") +
      immutable("Nil") +
      immutable("Map") +
      immutable("Seq") +
      immutable("Vector") +
      ctx.replace(
        from = Symbol(
          "_root_.scala.collection.immutable.Stream.`#::`.unapply(Lscala/collection/immutable/Stream;)Lscala/Option;."),
        to = q"#::",
        additionalImports =
          List(importer"strawman.collection.immutable.LazyList.#::")
      ) +
      ctx.replace(
        from = Symbol("_root_.scala.package.Stream."),
        to = q"LazyList",
        additionalImports =
          List(importer"strawman.collection.immutable.LazyList")
      )
  }
}
