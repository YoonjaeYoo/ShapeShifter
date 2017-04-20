package me.yoonjae.shapeshifter.poet

import java.io.Writer

open class Variable(val name: String) : Element() {

    private var let: Boolean = false
    private var type: String? = null
    private var value: String? = null

    fun let(let: Boolean): Variable {
        this.let = let
        return this
    }

    fun type(type: String): Variable {
        this.type = type
        return this
    }

    fun value(value: String): Variable {
        this.value = value
        return this
    }

    override fun render(writer: Writer, indentLevel: Int) {
        writer.write(if (let) "let " else "var ", indentLevel)
        writer.write("let $name", indentLevel)
        if (type != null) writer.write(": $type", indentLevel)
        writer.write(" = $value\n", indentLevel)
    }
}