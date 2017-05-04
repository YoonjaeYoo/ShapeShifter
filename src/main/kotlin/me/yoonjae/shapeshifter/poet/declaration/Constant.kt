package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import java.io.Writer

class Constant(val name: String, var value: String, var type: String? = null) : Declaration,
        DeclarationModifierDescriber {

    override var accessLevelModifier: AccessLevelModifier? = null
    override val declarationModifiers = mutableListOf<DeclarationModifier>()

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
        type.let { writer.write(" $it") }
        writer.write(" = $value")
    }
}

interface ConstantDescriber : Describer {
    val constants: MutableList<Constant>

    fun constant(name: String, value: String, type: String? = null,
                 init: (Constant.() -> Unit)? = null): Constant {
        val constant = Constant(name, value, type)
        init?.invoke(constant)
        constants.add(constant)
        return constant
    }
}
