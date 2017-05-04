package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Enum(val name: String) : Declaration, AccessLevelModifierDescriber {

    class Case(val name: String) : Element {

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.write("case ")
            writer.write(name)
        }
    }

    override var accessLevelModifier: AccessLevelModifier? = null
    val cases = mutableListOf<Case>()

    fun case(name: String) {
        val enumCase = Case(name)
        cases.add(enumCase)
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.writeln("enum $name {")
        cases.forEach {
            val prefix = (Indent(1) + linePrefix).apply { render(writer) }
            it.render(writer, prefix)
            writer.writeln()
        }
        linePrefix?.render(writer)
        writer.write("}")
    }
}


interface EnumDescriber : Describer {

    val enums: MutableList<Enum>

    fun enum(name: String, init: (Enum.() -> Unit)? = null): Enum {
        val enum = Enum(name)
        init?.invoke(enum)
        enums.add(enum)
        return enum
    }
}
