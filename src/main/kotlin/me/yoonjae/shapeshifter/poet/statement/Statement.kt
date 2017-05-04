package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

interface Statement : Element

interface StatementDescriber : ExpressionDescriber {

    val statements: MutableList<Statement>
    override val expressions: MutableList<in Expression>
        get() = statements
}

fun List<Statement>.render(writer: Writer, linePrefix: Element? = null) {
    forEach {
        linePrefix?.render(writer)
        it.render(writer, linePrefix)
        writer.writeln()
    }
}
