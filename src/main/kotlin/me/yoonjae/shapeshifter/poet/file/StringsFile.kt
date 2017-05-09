package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class StringsFile(init: (StringsFile.() -> Unit)? = null) : File {

    override var name: String = "Localizable.strings"
    private val strings = LinkedHashMap<String, String>()

    init {
        init?.invoke(this)
    }

    fun string(key: String, value: String) {
        strings.put(key, value)
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        for ((key, value) in strings) {
            writer.write("\"$key\" = \"$value\";\n")
        }
    }
}