package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Import(val name: String) : Declaration {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("import $name")
    }
}

interface ImportDescriber : Describer {

    val imports: MutableList<Import>

    fun import(name: String): Import {
        val import = Import(name)
        imports.add(import)
        return import
    }
}
