package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Argument(val name: String? = null, value: String? = null) : Element,
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    init {
        value?.let { generalExpression(value) }
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        name?.let { writer.write("$name: ") }
        expressions.first().render(writer, linePrefix)
    }
}

interface ArgumentDescriber : Describer {

    val arguments: MutableList<Argument>

    fun argument(name: String? = null, value: String? = null, init: (Argument.() -> Unit)? = null):
            Argument {
        val argument = Argument(name, value)
        init?.invoke(argument)
        arguments.add(argument)
        return argument
    }

    class Delegate : ArgumentDescriber {
        override val arguments = mutableListOf<Argument>()
    }
}

fun List<Argument>.render(writer: Writer, linePrefix: Element? = null) {
    writer.write("(")
    if (isNotEmpty()) {
        if (size == 1) {
            get(0).render(writer, linePrefix)
        } else {
            forEachIndexed { index, argument ->
                if (index > 0) writer.write(",")
                writer.writeln()
                (Indent(2) + linePrefix).render(writer)
                argument.render(writer, (Indent(2) + linePrefix))
            }
            writer.writeln()
            linePrefix?.render(writer)
        }
    }
    writer.write(")")
}
