package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class Constant(val name: String, var value: String? = null, var type: Type? = null) : Declaration,
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
        writer.write("let ")
        writer.write(name)
        type?.let {
            writer.write(": ")
            it.render(writer)
        }
        value?.let { writer.write(" = $it") }
    }
}

interface ConstantDescriber : Describer {

    val constants: MutableList<Constant>

    fun constant(name: String, value: String? = null, type: Type? = null,
                 init: (Constant.() -> Unit)? = null): Constant {
        val constant = Constant(name, value, type)
        init?.invoke(constant)
        constants.add(constant)
        return constant
    }

    class Delegate : ConstantDescriber {
        override val constants = mutableListOf<Constant>()
    }
}
