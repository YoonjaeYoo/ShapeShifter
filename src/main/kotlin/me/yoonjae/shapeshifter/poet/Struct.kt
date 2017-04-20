package me.yoonjae.shapeshifter.poet

import java.io.Writer

class Struct(val name: String) : Element() {

    private val fields = mutableListOf<Field>()

    fun addField(field: Field): Struct {
        fields.add(field)
        return this
    }

    override fun render(writer: Writer, indentLevel: Int) {
        writer.write("struct $name {\n", indentLevel)
        fields.forEach { it.render(writer, indentLevel + 1) }
        writer.write("}\n", indentLevel)
    }
}