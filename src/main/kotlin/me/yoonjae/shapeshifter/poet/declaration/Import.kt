package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Import(val name: String) : Declaration {

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        writer.writeln("import $name")
    }
}

interface ImportContainer {

    val imports: MutableList<Import>

    fun import(name: String): Import {
        val import = Import(name)
        imports.add(import)
        return import
    }
}
