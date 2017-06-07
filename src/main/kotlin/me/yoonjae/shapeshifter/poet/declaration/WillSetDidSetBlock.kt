package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.MutationModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class WillSetDidSetBlock : Element {

    var willSet: WillSetClause? = null
    var didSet: DidSetClause? = null

    fun willSet(name: String? = null, init: (WillSetClause.() -> Unit)? = null) {
        val willSet = WillSetClause(name)
        init?.invoke(willSet)
        this.willSet = willSet
    }

    fun didSet(name: String? = null, init: (DidSetClause.() -> Unit)? = null) {
        val didSet = DidSetClause(name)
        init?.invoke(didSet)
        this.didSet = didSet
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("{")
        willSet?.let {
            writer.writeln()
            (Indent(1) + linePrefix).render(writer)
            it.render(writer, Indent(1) + linePrefix)
        }
        didSet?.let {
            writer.writeln()
            (Indent(1) + linePrefix).render(writer)
            it.render(writer, Indent(1) + linePrefix)
        }
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }

    class WillSetClause(val name: String? = null) : CodeBlock(),
            MutationModifierDescriber by MutationModifierDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            mutationModifier?.let {
                it.render(writer)
                writer.write(" ")
            }
            writer.write("willSet")
            name?.let { writer.write("($it)") }
            writer.write(" ")
            super.render(writer, linePrefix)
        }
    }

    class DidSetClause(val name: String? = null) : CodeBlock(),
            MutationModifierDescriber by MutationModifierDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            mutationModifier?.let {
                it.render(writer)
                writer.write(" ")
            }
            writer.write("didSet")
            name?.let { writer.write("($it)") }
            writer.write(" ")
            super.render(writer, linePrefix)
        }
    }
}
