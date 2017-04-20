package me.yoonjae.shapeshifter.poet

import java.io.Writer

class SwiftFile {

    private val imports = mutableListOf<Import>()
    private val structs = mutableListOf<Struct>()

    fun addImport(import: Import): SwiftFile {
        imports.add(import)
        return this
    }

    fun addStruct(struct: Struct): SwiftFile {
        structs.add(struct)
        return this
    }

    fun writeTo(writer: Writer) {
        imports.forEach { it.render(writer) }
        writer.write("\n")
        structs.forEach { it.render(writer) }
    }
}