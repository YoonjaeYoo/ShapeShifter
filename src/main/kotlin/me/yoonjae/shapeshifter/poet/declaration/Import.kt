package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class Import(val name: String) : Declaration() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("import $name")
    }
}

interface ImportDescriber : Describer {

    val imports: MutableList<Import>

    fun import(name: String, init: (Import.() -> Unit)? = null): Import {
        val import = Import(name)
        init?.invoke(import)
        imports.add(import)
        return import
    }

    class Delegate : ImportDescriber {
        override val imports = mutableListOf<Import>()
    }
}
