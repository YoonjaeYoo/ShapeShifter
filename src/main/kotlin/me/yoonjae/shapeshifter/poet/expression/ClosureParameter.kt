package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class ClosureParameter(val name: String, var type: Type? = null) : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(name)
        type?.let {
            writer.write(": ")
            it.render(writer, linePrefix)
        }
    }
}

interface ClosureParameterDescriber : Describer {

    val closureParameters: MutableList<ClosureParameter>

    fun closureParameter(name: String, type: Type? = null,
                         init: (ClosureParameter.() -> Unit)? = null): ClosureParameter {
        val closureParameter = ClosureParameter(name, type)
        init?.invoke(closureParameter)
        closureParameters.add(closureParameter)
        return closureParameter
    }

    class Delegate : ClosureParameterDescriber {
        override val closureParameters = mutableListOf<ClosureParameter>()
    }
}

fun List<ClosureParameter>.render(writer: Writer, linePrefix: Element? = null) {
    if (isNotEmpty()) {
        writer.write("(")
        forEachIndexed { index, closureParameter ->
            if (index > 0) writer.write(", ")
            closureParameter.render(writer, linePrefix)
        }
        writer.write(")")
    }
}
