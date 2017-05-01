package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Constant(val name: String, val value: String, val type: String? = null) : Declaration,
        DeclarationModifierDescriber {

    override val declarationModifiers = mutableListOf<DeclarationModifier>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        declarationModifiers.forEach { it.render(writer) }
        writer.write("let ")
        writer.write(name)
        writer.write(if (type == null) "" else ": $type")
        writer.writeln(" = $value")
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
