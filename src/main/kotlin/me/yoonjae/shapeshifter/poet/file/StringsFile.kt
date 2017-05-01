package me.yoonjae.shapeshifter.poet.file

import java.io.Writer

class StringsFile : File {

    companion object {
        fun create(init: StringsFile.() -> Unit): StringsFile {
            val file = StringsFile()
            file.init()
            return file
        }
    }

    private val strings = LinkedHashMap<String, String>()

    fun string(key: String, value: String) {
        strings.put(key, value)
    }

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        for ((key, value) in strings) {
            beforeEachLine?.invoke(writer)
            writer.write("\"$key\" = \"$value\";\n")
        }
    }
}