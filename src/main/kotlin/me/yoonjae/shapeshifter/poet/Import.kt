package me.yoonjae.shapeshifter.poet

import java.io.Writer

class Import(val name: String) : Element() {

    override fun render(writer: Writer, indentLevel: Int) {
        writer.write("import $name\n", indentLevel)
    }
}
