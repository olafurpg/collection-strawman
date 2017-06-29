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
    val db = forCtx(ctx)
    def immutable(name: String,
                  immutable: Boolean = true,
                  pkg: Boolean = true) = {
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
            additionalImports =
              importer"strawman.collection.immutable.$rename" :: {
                if (pkg) importer"scala.{$rename => _}" :: Nil
                else Nil
              }
          )
      }
    }
    val intArrayOps =
      if (db.sugars.values.exists(_ == "scala.Predef.intArrayOps(*)")) {
        ctx.addGlobalImport(importer"scala.Predef.{intArrayOps => _}") +
          ctx.addGlobalImport(importer"strawman.collection.arrayToArrayOps")
      } else Patch.empty
    val arrayBufferSymbol = Symbol(
      "_root_.scala.collection.mutable.ArrayBuffer.")

    val arrayBuffer =
      if (db.names.values.exists(_ == arrayBufferSymbol)) {
        // TODO(olafur) add support to remove import by symbol.
        ctx.addGlobalImport(importer"strawman.collection.mutable.ArrayBuffer") ++
          ctx.tree.collect {
            case ee: Importee
                if ee.symbolOpt.exists(_.normalized == arrayBufferSymbol) =>
              ctx.removeImportee(ee)
          }
      } else Patch.empty
    arrayBuffer +
      intArrayOps +
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
