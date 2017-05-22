package me.yoonjae.shapeshifter.poet.type

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.GenericParameterDescriber
import me.yoonjae.shapeshifter.poet.declaration.render
import java.io.Writer

class Type(val name: String, var optional: Boolean = false) : Element,
        GenericParameterDescriber by GenericParameterDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        if (optional && name.contains(" ")) writer.write("(")
        writer.write(name)
        if (optional && name.contains(" ")) writer.write(")")
        if (optional) writer.write("?")
        genericParameters.render(writer, linePrefix)
    }
}

interface TypeDescriber : Describer {

    val types: MutableList<Type>

    fun type(name: String, optional: Boolean = false, init: (Type.() -> Unit)? = null):
            Type {
        val type = Type(name, optional)
        init?.invoke(type)
        types.add(type)
        return type
    }

    class Delegate : TypeDescriber {
        override val types = mutableListOf<Type>()
    }
}
