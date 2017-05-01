package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Variable(val name: String, val value: String? = null, val type: String? = null) :
        Declaration, DeclarationModifierDescriber {

    override val declarationModifiers = mutableListOf<DeclarationModifier>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        declarationModifiers.forEach { it.render(writer) }
        writer.write("var ")
        writer.write(name)
        writer.write(if (type == null) "" else ": $type")
        writer.writeln(if (value == null) "" else " = $value")
    }
}

interface VariableDescriber : Describer {

    val variables: MutableList<Variable>

    fun variable(name: String, value: String? = null, type: String? = null): Variable {
        val variable = Variable(name, value, type)
        variables.add(variable)
        return variable
    }
}
