package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.MutationModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class GetterSetterBlock : Element {

    var get: GetterClause = GetterClause()
    var set: SetterClause? = null

    fun get(init: (GetterClause.() -> Unit)? = null) {
        val get = GetterClause()
        init?.invoke(get)
        this.get = get
    }

    fun set(name: String? = null, init: (SetterClause.() -> Unit)? = null) {
        val set = SetterClause(name)
        init?.invoke(set)
        this.set = set
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.writeln("{")
        (Indent(1) + linePrefix).render(writer)
        get.render(writer, Indent(1) + linePrefix)
        set?.let {
            writer.writeln()
            (Indent(1) + linePrefix).render(writer)
            it.render(writer, Indent(1) + linePrefix)
        }
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }

    class GetterClause : CodeBlock(),
            MutationModifierDescriber by MutationModifierDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            mutationModifier?.let {
                it.render(writer)
                writer.write(" ")
            }
            writer.write("get ")
            super.render(writer, linePrefix)
        }
    }

    class SetterClause(val name: String? = null) : CodeBlock(),
            MutationModifierDescriber by MutationModifierDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            mutationModifier?.let {
                it.render(writer)
                writer.write(" ")
            }
            writer.write("set")
            name?.let { writer.write("($it)") }
            writer.write(" ")
            super.render(writer, linePrefix)
        }
    }
}
