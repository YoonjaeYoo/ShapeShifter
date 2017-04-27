package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class GenericParameter(val name: String) : Element {

    val superTypeNames = mutableListOf<String>()

    fun superType(name: String) {
        superTypeNames.add(name)
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        writer.write(name)
        if (superTypeNames.isNotEmpty()) {
            writer.write(": ")
            superTypeNames.forEachIndexed { index, superType ->
                if (index > 0) writer.write(" & ")
                writer.write(superType)
            }
        }
    }
}

interface GenericParameterContainer {

    val genericParameters: MutableList<GenericParameter>

    fun genericParameter(name: String, init: (GenericParameter.() -> Unit)? = null):
            GenericParameter {
        val genericParameter = GenericParameter(name)
        init?.invoke(genericParameter)
        genericParameters.add(genericParameter)
        return genericParameter
    }
}

fun List<GenericParameter>.render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
    if (isNotEmpty()) {
        writer.write("<")
        forEachIndexed { index, parameter ->
            if (index > 0) writer.write(", ")
            parameter.render(writer)
        }
        writer.write(">")
    }
}
