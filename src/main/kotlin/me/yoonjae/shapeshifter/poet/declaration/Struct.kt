package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Struct(val name: String) : Declaration(),
        AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate(),
        GenericParameterDescriber by GenericParameterDescriber.Delegate(),
        TypeInheritanceDescriber by TypeInheritanceDescriber.Delegate(),
        DeclarationDescriber by DeclarationDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("struct $name")
        genericParameters.render(writer, linePrefix)
        if (superTypes.isNotEmpty()) {
            writer.write(": ")
            superTypes.forEachIndexed { index, type ->
                if (index > 0) writer.write(", ")
                type.render(writer, linePrefix)
            }
        }
        writer.writeln(" {")
        imports.render(writer, linePrefix)
        typeAliases.render(writer, linePrefix)
        constants.render(writer, linePrefix)
        variables.render(writer, linePrefix)
        initializers.render(writer, linePrefix, true)
        functions.render(writer, linePrefix, true)
        enums.render(writer, linePrefix, true)
        structs.render(writer, linePrefix, true)
        classes.render(writer, linePrefix, true)
        protocols.render(writer, linePrefix, true)
        extensions.render(writer, linePrefix, true)
        linePrefix?.render(writer)
        writer.write("}")
    }

    private fun List<Declaration>.render(writer: Writer, linePrefix: Element? = null,
                                         newline: Boolean = false) {
        if (isNotEmpty()) {
            forEach {
                if (newline) writer.writeln()
                val prefix = (Indent(1) + linePrefix).apply { render(writer) }
                it.render(writer, prefix)
                writer.writeln()
            }
        }
    }
}

interface StructDescriber : Describer {

    val structs: MutableList<Struct>

    fun struct(name: String, init: (Struct.() -> Unit)? = null): Struct {
        val struct = Struct(name)
        init?.invoke(struct)
        structs.add(struct)
        return struct
    }

    class Delegate : StructDescriber {
        override val structs = mutableListOf<Struct>()
    }
}
