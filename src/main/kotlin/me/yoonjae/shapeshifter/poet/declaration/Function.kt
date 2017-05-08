package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Function(val name: String, var result: Type? = null) : Declaration,
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        GenericParameterDescriber by GenericParameterDescriber.Delegate(),
        ParameterDescriber by ParameterDescriber.Delegate(),
        StatementDescriber by StatementDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("func $name")
        genericParameters.render(writer, linePrefix)
        parameters.render(writer, linePrefix)
        writer.write(" ")
        result?.let {
            writer.write("-> ")
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.writeln("{")
        statements.forEach {
            val prefix = (Indent(1) + linePrefix).apply { render(writer) }
            it.render(writer, prefix)
            writer.writeln()
        }
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface FunctionDescriber : Describer {

    val functions: MutableList<Function>

    fun function(name: String, result: Type? = null, init: (Function.() -> Unit)? = null):
            Function {
        val function = Function(name, result)
        init?.invoke(function)
        functions.add(function)
        return function
    }

    class Delegate : FunctionDescriber {
        override val functions = mutableListOf<Function>()
    }
}
