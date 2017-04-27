package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class Parameter(val name: String?, val type: String, val label: String? = null,
                val value: String? = null) : Element {

    private var inout: Boolean = false
    private var variadic: Boolean = false

    fun inout(inout: Boolean) {
        this.inout = inout
    }

    fun variadic(variadic: Boolean) {
        this.variadic = variadic
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        writer.write(label ?: "_")
        writer.write(" ")
        writer.write(name ?: "")
        writer.write(": $type")
        writer.write(if (variadic) "..." else "")
        if (!variadic && value != null) writer.write(" = $value")
    }
}

interface ParameterContainer {

    val parameters: MutableList<Parameter>

    fun parameter(name: String?, type: String, label: String? = null, value: String? = null,
                  init: (Parameter.() -> Unit)? = null):
            Parameter {
        val parameter = Parameter(name, type, label, value)
        init?.invoke(parameter)
        parameters.add(parameter)
        return parameter
    }
}