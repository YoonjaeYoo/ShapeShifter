package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import java.io.Writer

class Variable(val name: String, var value: String? = null, var type: String? = null) :
        Declaration, DeclarationModifierDescriber {

    override var accessLevelModifier: AccessLevelModifier? = null
    override val declarationModifiers = mutableListOf<DeclarationModifier>()

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier.let {
            render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("var ")
        writer.write(name)
        type.let { writer.write(": $type") }
        value.let { writer.write(" = $value") }
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
