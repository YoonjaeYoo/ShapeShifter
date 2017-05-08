package me.yoonjae.shapeshifter.poet.type

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.GenericParameterDescriber
import me.yoonjae.shapeshifter.poet.declaration.render
import java.io.Writer

class Type(val name: String) : Element,
        GenericParameterDescriber by GenericParameterDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(name)
        genericParameters.render(writer, linePrefix)
    }
}