package me.yoonjae.shapeshifter.poet.file

import java.io.Writer

class StringsFile : File {

    private val strings = LinkedHashMap<String, String>()

    fun addString(key: String, value: String) {
        strings.put(key, value)
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        for ((key, value) in strings) {
            beforeEachLine?.invoke(writer)
            writer.write("\"$key\" = \"$value\";\n")
        }
    }
}