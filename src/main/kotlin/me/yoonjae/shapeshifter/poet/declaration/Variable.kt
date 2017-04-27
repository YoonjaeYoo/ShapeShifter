package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Variable(val name: String, val value: String? = null, val type: String? = null) :
        Declaration {

    private val modifiers = mutableListOf<DeclarationModifier>()

    fun modifier(modifier: DeclarationModifier) {
        modifiers.add(modifier)
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        modifiers.forEachIndexed { index, modifier ->
            modifier.render(writer, if (index == 0) beforeEachLine else null)
        }
        writer.write("var ")
        writer.write(name)
        writer.write(if (type == null) "" else ": $type")
        writer.writeln(if (value == null) "" else " = $value")
    }
}

interface VariableContainer {

    val variables: MutableList<Variable>

    fun variable(name: String, value: String? = null, type: String? = null): Variable {
        val variable = Variable(name, value, type)
        variables.add(variable)
        return variable
    }
}
