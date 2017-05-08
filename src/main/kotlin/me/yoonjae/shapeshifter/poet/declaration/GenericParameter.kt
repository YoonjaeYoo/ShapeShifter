package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class GenericParameter(val name: String) : Element, Inheritable by Inheritable.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(name)
        if (superTypes.isNotEmpty()) {
            writer.write(":")
            superTypes.forEachIndexed { index, superType ->
                if (index > 0) writer.write(" & ")
                writer.write(superType.name)
            }
        }
    }
}

interface GenericParameterDescriber : Describer {

    val genericParameters: MutableList<GenericParameter>
        get() = mutableListOf()

    fun genericParameter(name: String, init: (GenericParameter.() -> Unit)? = null):
            GenericParameter {
        val genericParameter = GenericParameter(name)
        init?.invoke(genericParameter)
        genericParameters.add(genericParameter)
        return genericParameter
    }

    class Delegate : GenericParameterDescriber {
        override val genericParameters = mutableListOf<GenericParameter>()
    }
}

fun List<GenericParameter>.render(writer: Writer, linePrefix: Element? = null) {
    if (isNotEmpty()) {
        writer.write("<")
        forEachIndexed { index, parameter ->
            if (index > 0) writer.write(", ")
            parameter.render(writer, linePrefix)
        }
        writer.write(">")
    }
}
