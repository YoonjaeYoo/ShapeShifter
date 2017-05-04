package me.yoonjae.shapeshifter.poet

import java.io.Writer

interface Element {

    fun render(writer: Writer, linePrefix: Element? = null)

    operator fun plus(prefix: Element?): Element {
        if (prefix == null) {
            return this
        } else {
            return object : Element {
                override fun render(writer: Writer, linePrefix: Element?) {
                    prefix.render(writer, linePrefix)
                    this@Element.render(writer)
                }
            }
        }
    }
}

fun Writer.writeln() {
    write("\n")
}

fun Writer.writeln(str: String) {
    write("$str\n")
}
