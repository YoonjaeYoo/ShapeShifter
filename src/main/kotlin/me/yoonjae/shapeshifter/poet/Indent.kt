package me.yoonjae.shapeshifter.poet

import java.io.Writer

class Indent(val level: Int) : Element {

    companion object {
        var size = 4
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        for (i in 0 until level * size) {
            writer.write(" ")
        }
    }
}