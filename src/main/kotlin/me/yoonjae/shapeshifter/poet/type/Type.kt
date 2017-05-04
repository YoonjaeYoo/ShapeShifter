package me.yoonjae.shapeshifter.poet.type

import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class Type(val name: String) : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(name)
    }
}