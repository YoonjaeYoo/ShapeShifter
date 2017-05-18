package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

open class CodeBlock : Element, StatementDescriber by StatementDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.writeln("{")
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface CodeBlockDescriber : Describer {

    var codeBlock: CodeBlock?

    fun codeBlock(init: (CodeBlock.() -> Unit)? = null): CodeBlock {
        val codeBlock = CodeBlock()
        init?.invoke(codeBlock)
        this.codeBlock = codeBlock
        return codeBlock
    }

    class Delegate : CodeBlockDescriber {
        override var codeBlock: CodeBlock? = null
    }
}
