package me.yoonjae.shapeshifter.poet

import java.io.Writer

interface Element {
    fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)? = null)
}

fun Writer.writeln(str: String) {
    write("$str\n")
}

fun String.toCamelCase(): String {
    val builder = StringBuilder()
    var underscore = false
    for (c in toCharArray()) {
        if (c == '_') {
            underscore = true
        } else if (underscore) {
            builder.append(c.toUpperCase())
            underscore = false
        } else {
            builder.append(c)
        }
    }
    return builder.toString()
}
