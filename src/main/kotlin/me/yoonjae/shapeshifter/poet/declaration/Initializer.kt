package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.statement.Statement
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Initializer(override var accessLevelModifier: AccessLevelModifier? = null) : Declaration,
        DeclarationModifierDescriber, ParameterDescriber, StatementDescriber {

    override val declarationModifiers = mutableListOf<DeclarationModifier>()
    override val parameters = mutableListOf<Parameter>()
    override val statements = mutableListOf<Statement>()

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("init")
        parameters.render(writer, linePrefix)
        writer.writeln(" {")
        statements.render(writer, Indent(1) + linePrefix)
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface InitializerDescriber : Describer {

    val initializers: MutableList<Initializer>

    fun initializer(accessLevelModifier: AccessLevelModifier? = null,
                    init: (Initializer.() -> Unit)? = null): Initializer {
        val initializer = Initializer(accessLevelModifier)
        init?.invoke(initializer)
        initializers.add(initializer)
        return initializer
    }
}
