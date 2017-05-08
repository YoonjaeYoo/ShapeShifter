package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Parameter(val name: String?, var type: Type, var label: String? = null,
                var variadic: Boolean = false) : Element,
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        label?.let { writer.write("$label ") }
        name?.let { writer.write(name) }
        writer.write(": ")
        type.render(writer)
        if (variadic) writer.write("...")
        if (expressions.isNotEmpty()) {
            writer.write(" = ")
            expressions.first().render(writer, linePrefix)
        }
    }
}

interface ParameterDescriber : Describer {

    val parameters: MutableList<Parameter>

    fun parameter(name: String?, type: Type, label: String? = null, variadic: Boolean = false,
                  init: (Parameter.() -> Unit)? = null): Parameter {
        val parameter = Parameter(name, type, label, variadic)
        init?.invoke(parameter)
        parameters.add(parameter)
        return parameter
    }

    fun parameter(name: String?, type: String, label: String? = null, variadic: Boolean = false,
                  init: (Parameter.() -> Unit)? = null): Parameter {
        val parameter = Parameter(name, Type(type), label, variadic)
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
    if (isNotEmpty()) {
        forEachIndexed { index, parameter ->
            if (index > 0) writer.write(", ")
            writer.writeln()
            (Indent(2) + linePrefix).render(writer)
            parameter.render(writer, (Indent(2) + linePrefix))
        }
        writer.writeln()
        linePrefix?.render(writer)
    }
    writer.write(")")
}
