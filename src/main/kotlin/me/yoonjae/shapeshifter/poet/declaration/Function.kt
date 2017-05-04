package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.statement.Statement
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Function(val name: String, override var accessLevelModifier: AccessLevelModifier? = null,
               var result: Type? = null) : Declaration, DeclarationModifierDescriber,
        GenericParameterDescriber, ParameterDescriber, StatementDescriber {

    override val declarationModifiers = mutableListOf<DeclarationModifier>()
    override val genericParameters = mutableListOf<GenericParameter>()
    override val parameters = mutableListOf<Parameter>()
    override val statements = mutableListOf<Statement>()

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer)
            writer.write(" ")
        }
        writer.write("func $name")
        genericParameters.render(writer)
        writer.write(" ")
        parameters.render(writer)
        writer.write(" ")
        result?.let {
            writer.write("-> ")
            it.render(writer)
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

    fun function(name: String, accessLevelModifier: AccessLevelModifier? = null,
                 result: Type? = null, init: (Function.() -> Unit)? = null): Function {
        val function = Function(name, accessLevelModifier, result)
        init?.invoke(function)
        functions.add(function)
        return function
    }
}
