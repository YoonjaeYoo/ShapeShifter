package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Enum(val name: String) : Declaration {

    class EnumCase(val name: String) : Element {
        override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
            beforeEachLine?.invoke(writer)
            writer.write("case ")
            writer.writeln(name)
        }
    }

    val cases = mutableListOf<EnumCase>()

    fun case(name: String) {
        val enumCase = EnumCase(name)
        cases.add(enumCase)
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        writer.write("enum ")
        writer.write(name)
        writer.writeln(" {")
        cases.forEach {
            it.render(writer) { writer ->
                beforeEachLine?.invoke(writer)
                Indent(1).render(writer)
            }
        }
        beforeEachLine?.invoke(writer)
        writer.writeln("}")
    }
}

interface EnumContainer {

    val enums: MutableList<Enum>

    fun enum(name: String, init: (Enum.() -> Unit)? = null): Enum {
        val enum = Enum(name)
        init?.invoke(enum)
        enums.add(enum)
        return enum
    }
}