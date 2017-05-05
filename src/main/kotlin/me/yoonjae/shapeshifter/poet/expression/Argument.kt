package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class Argument(val name: String?, var value: Expression) : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        name?.let { writer.write("$name: ") }
        value.render(writer, linePrefix)
    }
}

interface ArgumentDescriber : Describer {

    val arguments: MutableList<Argument>

    fun argument(name: String?, value: String): Argument {
        return argument(name, GeneralExpression(value))
    }

    fun argument(name: String?, value: Expression): Argument {
        val argument = Argument(name, value)
        arguments.add(argument)
        return argument
    }

    class Delegate : ArgumentDescriber {
        override val arguments = mutableListOf<Argument>()
    }
}

fun List<Argument>.render(writer: Writer, linePrefix: Element? = null) {
    if (isNotEmpty()) {
        writer.write("(")
        forEachIndexed { index, argument ->
            if (index > 0) writer.write(", ")
            argument.render(writer, linePrefix)
        }
        writer.write(")")
    }
}
