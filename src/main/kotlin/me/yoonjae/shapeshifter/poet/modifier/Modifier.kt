package me.yoonjae.shapeshifter.poet.modifier

import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

abstract class Modifier(val name: String) : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(name)
    }
}
