package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class Parameter(val name: String?, var type: Type, var label: String? = null,
                var value: Expression? = null, var variadic: Boolean = false) : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        label?.let { writer.write("$label ") }
        name?.let { writer.write(name) }
        writer.write(": $type")
        if (variadic) writer.write("...")
        value?.render(writer)
    }
}

interface ParameterDescriber : Describer {

    val parameters: MutableList<Parameter>

    fun parameter(name: String?, type: Type, label: String? = null, value: Expression? = null,
                  variadic: Boolean = false, init: (Parameter.() -> Unit)? = null): Parameter {
        val parameter = Parameter(name, type, label, value, variadic)
        init?.invoke(parameter)
        parameters.add(parameter)
        return parameter
    }

    class Delegate : ParameterDescriber {
        override val parameters = mutableListOf<Parameter>()
    }
}

fun List<Parameter>.render(writer: Writer, linePrefix: Element? = null) {
    writer.write("(")
    forEachIndexed { index, parameter ->
        if (index > 0) writer.write(", ")
        parameter.render(writer, linePrefix)
    }
    writer.write(")")
}
