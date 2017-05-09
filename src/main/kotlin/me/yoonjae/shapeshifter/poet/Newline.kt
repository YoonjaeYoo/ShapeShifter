package me.yoonjae.shapeshifter.poet

import java.io.Writer

class Newline : Element {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.writeln()
    }
}