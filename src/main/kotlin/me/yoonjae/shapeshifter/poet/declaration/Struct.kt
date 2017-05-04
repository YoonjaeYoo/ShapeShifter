package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Struct(val name: String, override var superClass: Type? = null) : Declaration, Inheritable,
        AccessLevelModifierDescriber, GenericParameterDescriber, DeclarationDescriber {

    override var accessLevelModifier: AccessLevelModifier? = null
    override val genericParameters = mutableListOf<GenericParameter>()
    override val imports = mutableListOf<Import>()
    override val typeAliases = mutableListOf<TypeAlias>()
    override val constants = mutableListOf<Constant>()
    override val variables = mutableListOf<Variable>()
    override val initializers = mutableListOf<Initializer>()
    override val functions = mutableListOf<Function>()
    override val enums = mutableListOf<Enum>()
    override val structs = mutableListOf<Struct>()
    override val classes = mutableListOf<Class>()

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("struct $name")
        genericParameters.render(writer, linePrefix)
        superClass?.let {
            writer.write(": ")
            it.render(writer, linePrefix)
        }
        writer.writeln(" {")
        writer.writeln()
        imports.render(writer, linePrefix)
        typeAliases.render(writer, linePrefix)
        constants.render(writer, linePrefix)
        variables.render(writer, linePrefix)
        initializers.render(writer, linePrefix)
        functions.render(writer, linePrefix)
        enums.render(writer, linePrefix)
        structs.render(writer, linePrefix)
        classes.render(writer, linePrefix)
        linePrefix?.render(writer)
        writer.write("}")
    }

    private fun List<Declaration>.render(writer: Writer, linePrefix: Element? = null) {
        if (isNotEmpty()) {
            val prefix = (Indent(1) + linePrefix).apply { render(writer) }
            forEach {
                it.render(writer, prefix)
                writer.writeln()
            }
            writer.writeln()
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
}
