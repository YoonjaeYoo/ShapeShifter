package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Initializer(var optional: Boolean = false) : Declaration,
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        ParameterDescriber by ParameterDescriber.Delegate(),
        StatementDescriber by StatementDescriber.Delegate() {

    fun optional(optional: Boolean) {
        this.optional = optional
    }

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
        if (optional) {
            writer.write("?")
        }
        parameters.render(writer, linePrefix)
        writer.writeln(" {")
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface InitializerDescriber : Describer {

    val initializers: MutableList<Initializer>

    fun initializer(optional: Boolean = false, init: (Initializer.() -> Unit)? = null):
            Initializer {
        val initializer = Initializer(optional)
        init?.invoke(initializer)
        initializers.add(initializer)
        return initializer
    }

    class Delegate : InitializerDescriber {
        override val initializers = mutableListOf<Initializer>()
    }
}
