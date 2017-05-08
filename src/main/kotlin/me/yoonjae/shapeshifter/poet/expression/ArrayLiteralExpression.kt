package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class ArrayLiteralExpression : LiteralExpression,
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.writeln("[")
        expressions.forEachIndexed { index, expression ->
            if (index > 0) writer.writeln(", ")
            (Indent(2) + linePrefix).render(writer)
            expression.render(writer, (Indent(2) + linePrefix))
        }
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("]")
    }
}

interface ArrayLiteralExpressionDescriber : Describer {

    val arrayLiteralExpressions: MutableList<ArrayLiteralExpression>

    fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)? = null):
            ArrayLiteralExpression {
        val arrayLiteralExpression = ArrayLiteralExpression()
        init?.invoke(arrayLiteralExpression)
        arrayLiteralExpressions.add(arrayLiteralExpression)
        return arrayLiteralExpression
    }

    class Delegate : ArrayLiteralExpressionDescriber {
        override val arrayLiteralExpressions = mutableListOf<ArrayLiteralExpression>()
    }
}