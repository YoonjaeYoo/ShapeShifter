package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Class(val name: String, val superClassName: String? = null) : Declaration,
        GenericParameterContainer, DeclarationContainer {

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
        writer.write("class $name")
        genericParameters.render(writer, beforeEachLine)
        writer.write(if (superClassName == null) "" else ": $superClassName")
        writer.writeln(" {")
        writer.writeln("")
        renderDeclarations(beforeEachLine, writer)
        beforeEachLine?.invoke(writer)
        writer.writeln("}")
    }

    private fun renderDeclarations(beforeEachLine: ((Writer) -> Unit)?, writer: Writer) {
        val beforeEachDeclarationLine: (Writer) -> Unit = {
            beforeEachLine?.invoke(it)
            Indent(1).render(writer)
        }
        imports.render(writer, beforeEachDeclarationLine)
        typeAliases.render(writer, beforeEachDeclarationLine)
        constants.render(writer, beforeEachDeclarationLine)
        initializers.render(writer, beforeEachDeclarationLine)
        variables.render(writer, beforeEachDeclarationLine)
        functions.render(writer, beforeEachDeclarationLine)
        enums.render(writer, beforeEachDeclarationLine)
        structs.render(writer, beforeEachDeclarationLine)
        classes.render(writer, beforeEachDeclarationLine)
    }
}

interface ClassContainer {

    val classes: MutableList<Class>

    fun clazz(name: String, superClassName: String, init: (Class.() -> Unit)? = null): Class {
        val clazz = Class(name, superClassName)
        init?.invoke(clazz)
        classes.add(clazz)
        return clazz
    }
}