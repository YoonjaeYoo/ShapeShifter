package me.yoonjae.shapeshifter.poet

import me.yoonjae.shapeshifter.ShapeShifter
import java.io.Writer

abstract class Element {
    abstract fun render(writer: Writer, indentLevel: Int = 0)
}

fun Writer.write(string: String, indentLevel: Int) {
    for (i in 0 until (ShapeShifter.indentWidth * indentLevel)) {
        write(" ")
    }
    write(string)
}
