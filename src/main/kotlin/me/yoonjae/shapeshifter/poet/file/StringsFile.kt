package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.Element
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

    override fun render(writer: Writer, linePrefix: Element?) {
        for ((key, value) in strings) {
            writer.write("\"$key\" = \"$value\";\n")
        }
    }
}