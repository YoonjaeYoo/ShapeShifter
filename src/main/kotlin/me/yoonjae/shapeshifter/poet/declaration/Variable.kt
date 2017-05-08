package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class Variable(val name: String, var value: String? = null, var type: Type? = null) : Declaration,
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("var ")
        writer.write(name)
        type?.let {
            writer.write(": ")
            it.render(writer)
        }
        value?.let { writer.write(" = $it") }
    }
}

interface VariableDescriber : Describer {

    val variables: MutableList<Variable>

    fun variable(name: String, value: String? = null, type: Type? = null,
                 init: (Variable.() -> Unit)? = null): Variable {
        val variable = Variable(name, value, type)
        init?.invoke(variable)
        variables.add(variable)
        return variable
    }

    class Delegate : VariableDescriber {
        override val variables = mutableListOf<Variable>()
    }
}
