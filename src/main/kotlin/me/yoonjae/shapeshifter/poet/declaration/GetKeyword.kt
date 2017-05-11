package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.MutationModifierDescriber
import java.io.Writer

class GetKeyword : Element,
        MutationModifierDescriber by MutationModifierDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        mutationModifier?.let {
            it.render(writer)
            writer.write(" ")
        }
        writer.write("get")
    }
}

