package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Function(val name: String) : Declaration,
        DeclarationModifierDescriber, GenericParameterDescriber, ParameterDescriber {

    override val declarationModifiers = mutableListOf<DeclarationModifier>()
    override val genericParameters = mutableListOf<GenericParameter>()
    override val parameters = mutableListOf<Parameter>()
    private var returnType: String? = null

    fun returnType(returnType: String?) {
        this.returnType = returnType
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        declarationModifiers.forEach { it.render(writer) }
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

interface FunctionDescriber : Describer {

    val functions: MutableList<Function>

    fun function(name: String, init: (Function.() -> Unit)? = null):
            Function {
        val function = Function(name)
        init?.invoke(function)
        functions.add(function)
        return function
    }
}
