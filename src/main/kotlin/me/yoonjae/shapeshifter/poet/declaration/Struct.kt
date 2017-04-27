package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Struct(val name: String) : Declaration, GenericParameterContainer, DeclarationContainer {

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

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        writer.write("struct $name")
        genericParameters.render(writer, beforeEachLine)
        writer.writeln(" {")
        renderDeclarations(beforeEachLine, writer)
        beforeEachLine?.invoke(writer)
        writer.writeln("}")
    }

    private fun renderDeclarations(beforeEachLine: ((Writer) -> Unit)?, writer: Writer) {
        writer.writeln("")
        val beforeEachDeclarationLine: (Writer) -> Unit = {
            beforeEachLine?.invoke(it)
            Indent(1).render(writer)
        }
        imports.render(writer, beforeEachDeclarationLine)
        typeAliases.render(writer, beforeEachDeclarationLine)
        constants.render(writer, beforeEachDeclarationLine)
        variables.render(writer, beforeEachDeclarationLine)
        initializers.render(writer, beforeEachDeclarationLine)
        functions.render(writer, beforeEachDeclarationLine)
        enums.render(writer, beforeEachDeclarationLine)
        structs.render(writer, beforeEachDeclarationLine)
        classes.render(writer, beforeEachDeclarationLine)
    }
}

interface StructContainer {

    val structs: MutableList<Struct>

    fun struct(name: String, init: (Struct.() -> Unit)? = null): Struct {
        val struct = Struct(name)
        init?.invoke(struct)
        structs.add(struct)
        return struct
    }
}