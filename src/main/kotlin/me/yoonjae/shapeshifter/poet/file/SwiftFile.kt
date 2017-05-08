package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.Declaration
import me.yoonjae.shapeshifter.poet.declaration.DeclarationDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class SwiftFile(override var name: String) :
        File, DeclarationDescriber by DeclarationDescriber.Delegate() {

    constructor(name: String, init: (SwiftFile.() -> Unit)? = null) : this(name) {
        init?.invoke(this)
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        imports.render(writer)
        typeAliases.render(writer)
        constants.render(writer)
        variables.render(writer)
        functions.render(writer)
        enums.render(writer)
        structs.render(writer)
        classes.render(writer)
    }

    private fun List<Declaration>.render(writer: Writer, linePrefix: Element? = null) {
        if (isNotEmpty()) {
            linePrefix?.render(writer)
            forEach {
                it.render(writer, linePrefix)
                writer.writeln()
            }
            writer.writeln()
        }
    }
}