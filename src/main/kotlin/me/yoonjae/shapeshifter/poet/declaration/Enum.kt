package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.type.TypeDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Enum(val name: String) : Declaration(),
        TypeInheritanceDescriber by TypeInheritanceDescriber.Delegate(),
        AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate() {

    class Case(val name: String, val value: String? = null) : Element,
            TypeDescriber by TypeDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.write("case ")
            writer.write(name)
            if (types.isNotEmpty()) {
                writer.write("(")
                types.forEachIndexed { index, type ->
                    if (index > 0) {
                        writer.write(", ")
                    }
                    type.render(writer, Indent(2) + linePrefix)
                }
                writer.write(")")
            }
            value?.let {
                writer.write(" = $it")
            }
        }
    }

    val cases = mutableListOf<Case>()

    fun case(name: String, value: String? = null, init: (Case.() -> Unit)? = null) {
        val case = Case(name, value)
        init?.invoke(case)
        cases.add(case)
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("enum $name")
        if (superTypes.isNotEmpty()) {
            writer.write(": ")
            superTypes.forEachIndexed { index, type ->
                if (index > 0) writer.write(", ")
                type.render(writer, linePrefix)
            }
        }
        writer.writeln(" {")
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

    class Delegate : EnumDescriber {
        override val enums = mutableListOf<Enum>()
    }
}
