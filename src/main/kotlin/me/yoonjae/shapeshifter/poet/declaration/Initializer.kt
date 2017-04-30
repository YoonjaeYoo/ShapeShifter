package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.modifier.Modifier
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Initializer : Declaration, Modifier.Describer, Parameter.Describer {

    override val modifiers = mutableListOf<Modifier>()
    override val parameters = mutableListOf<Parameter>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        modifiers.forEach { it.render(writer) }
        writer.write("init(")
        parameters.forEachIndexed { index, parameter ->
            writer.write(if (index > 0) ", " else "")
            parameter.render(writer)
        }
        writer.write(") ")
        writer.writeln("{")
        beforeEachLine?.invoke(writer)
        // TODO
        writer.writeln("")
        beforeEachLine?.invoke(writer)
        writer.writeln("}")
    }
}

interface InitializerDescriber : Describer {

    val initializers: MutableList<Initializer>

    fun initializer(init: (Initializer.() -> Unit)? = null):
            Initializer {
        val initializer = Initializer()
        init?.invoke(initializer)
        initializers.add(initializer)
        return initializer
    }
}
