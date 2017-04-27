package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.modifier.Modifier
import me.yoonjae.shapeshifter.poet.modifier.ModifierContainer
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Function(val name: String, val returnType: String? = null) : Declaration,
        ModifierContainer, GenericParameterContainer, ParameterContainer {

    override val modifiers = mutableListOf<Modifier>()
    override val genericParameters = mutableListOf<GenericParameter>()
    override val parameters = mutableListOf<Parameter>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        modifiers.forEach { it.render(writer) }
        writer.write("func ")
        writer.write(name)
        if (genericParameters.isNotEmpty()) {
            writer.write("<")
            genericParameters.forEach { it.render(writer) }
            writer.write(">")
        }
        writer.write("(")
        parameters.forEachIndexed { index, parameter ->
            writer.write(if (index > 0) ", " else "")
            parameter.render(writer)
        }
        writer.write(") ")
        if (returnType != null) writer.write("-> $returnType ")
        writer.writeln("{")
        beforeEachLine?.invoke(writer)
        // TODO
        writer.writeln("")
        beforeEachLine?.invoke(writer)
        writer.writeln("}")
    }
}

interface FunctionContainer {

    val functions: MutableList<Function>

    fun function(name: String, returnType: String? = null, init: (Function.() -> Unit)? = null):
            Function {
        val function = Function(name, returnType)
        init?.invoke(function)
        functions.add(function)
        return function
    }
}