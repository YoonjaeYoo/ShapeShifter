package me.yoonjae.shapeshifter.poet

import java.io.Writer

open class Field(val name: String) : Element() {

    private var static: Boolean = false
    private var let: Boolean = false
    private var type: String? = null
    private var value: String? = null

    fun static(static: Boolean): Field {
        this.static = static
        return this
    }

    fun let(let: Boolean): Field {
        this.let = let
        return this
    }

    fun type(type: String): Field {
        this.type = type
        return this
    }

    fun value(value: String): Field {
        this.value = value
        return this
    }

    override fun render(writer: Writer, indentLevel: Int) {
        if (static) writer.write("static ", indentLevel)
        writer.write(if (let) "let " else "var ")
        writer.write(name)
        if (type != null) writer.write(": $type")
        writer.write(" = $value\n")
    }
}
