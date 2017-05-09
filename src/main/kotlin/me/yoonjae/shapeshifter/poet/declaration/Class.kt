package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Class(val name: String) : Declaration,
        AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate(),
        GenericParameterDescriber by GenericParameterDescriber.Delegate(),
        Inheritable by Inheritable.Delegate(),
        DeclarationDescriber by DeclarationDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("class $name")
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

interface ClassDescriber : Describer {

    val classes: MutableList<Class>

    fun clazz(name: String, init: (Class.() -> Unit)? = null): Class {
        val clazz = Class(name)
        init?.invoke(clazz)
        classes.add(clazz)
        return clazz
    }

    class Delegate : ClassDescriber {
        override val classes = mutableListOf<Class>()
    }
}
