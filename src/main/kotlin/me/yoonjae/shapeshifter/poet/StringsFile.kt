package me.yoonjae.shapeshifter.poet

import java.io.Writer

class StringsFile : File {

    private val strings = LinkedHashMap<String, String>()

    fun addString(key: String, value: String) {
        strings.put(key, value)
    }

    override fun writeTo(writer: Writer) {
        for ((key, value) in strings) {
            writer.write("\"$key\" = \"$value\";\n")
        }
    }
}