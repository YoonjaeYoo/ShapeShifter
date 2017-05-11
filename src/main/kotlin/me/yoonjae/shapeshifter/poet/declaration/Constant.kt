package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class Constant(val name: String, var type: Type? = null, value: String? = null) : Declaration,
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    init {
        value?.let { generalExpression(it) }
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
        writer.write("let ")
        writer.write(name)
        type?.let {
            writer.write(": ")
            it.render(writer)
        }
        if (expressions.isNotEmpty()) {
            writer.write(" = ")
            expressions.first().render(writer, linePrefix)
        }
    }
}

interface ConstantDescriber : Describer {

    val constants: MutableList<Constant>

    fun constant(name: String, type: Type? = null, value: String? = null,
                 init: (Constant.() -> Unit)? = null): Constant {
        val constant = Constant(name, type, value)
        init?.invoke(constant)
        constants.add(constant)
        return constant
    }

    class Delegate : ConstantDescriber {
        override val constants = mutableListOf<Constant>()
    }
}
