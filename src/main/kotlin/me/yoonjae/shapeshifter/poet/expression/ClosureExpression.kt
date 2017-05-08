package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class ClosureExpression : Expression,
        ClosureParameterDescriber by ClosureParameterDescriber.Delegate(),
        StatementDescriber by StatementDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("{ ")
        closureParameters.render(writer, linePrefix)
        writer.writeln(" in")
        statements.render(writer, Indent(1) + linePrefix)
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface ClosureExpressionDescriber : Describer {

    val closureExpressions: MutableList<ClosureExpression>

    fun closureExpression(init: (ClosureExpression.() -> Unit)? = null): ClosureExpression {
        val closureExpression = ClosureExpression()
        init?.invoke(closureExpression)
        closureExpressions.add(closureExpression)
        return closureExpression
    }

    class Delegate : ClosureExpressionDescriber {
        override val closureExpressions = mutableListOf<ClosureExpression>()
    }
}