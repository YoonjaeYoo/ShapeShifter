package me.yoonjae.shapeshifter.poet

import java.io.Writer

abstract class Element {
    abstract fun render(writer: Writer, indentLevel: Int = 0)
}

fun Writer.write(string: String, indentLevel: Int) {
    for (i in 0 until (4 * indentLevel)) {
        write(" ")
    }
    write(string)
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
