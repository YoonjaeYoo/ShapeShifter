package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.Declaration
import me.yoonjae.shapeshifter.poet.declaration.DeclarationDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

open class SwiftFile(override var name: String, init: (SwiftFile.() -> Unit)? = null) : File,
        DeclarationDescriber by DeclarationDescriber.Delegate() {

    init {
        init?.invoke(this)
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        imports.render(writer)
        typeAliases.render(writer)
        constants.render(writer)
        variables.render(writer)
        functions.render(writer)
        enums.render(writer)
        structs.render(writer, true)
        classes.render(writer, true)
        protocols.render(writer, true)
        extensions.render(writer, true)
    }

    private fun List<Declaration>.render(writer: Writer, newline: Boolean = false) {
        if (isNotEmpty()) {
            forEach {
                if (newline) writer.writeln()
                it.render(writer)
                writer.writeln()
            }
        }
    }
}