package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class ClosureExpression(var result: Type? = null) : Expression(),
        ClosureParameterDescriber by ClosureParameterDescriber.Delegate(),
        StatementDescriber by StatementDescriber.Delegate() {

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        writer.write("{")
        if (closureParameters.isNotEmpty()) {
            writer.write(" (")
            closureParameters.render(writer, linePrefix)
            writer.write(")")
        }
        result?.let {
            writer.write(" -> ")
            it.render(writer, linePrefix)
        }
        if (closureParameters.isNotEmpty() || result != null) {
            writer.writeln(" in")
        } else {
            writer.writeln()
        }
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface ClosureExpressionDescriber : Describer {

    val closureExpressions: MutableList<ClosureExpression>

    fun closureExpression(result: Type? = null, init: (ClosureExpression.() -> Unit)? = null):
            ClosureExpression {
        val closureExpression = ClosureExpression(result)
        init?.invoke(closureExpression)
        closureExpressions.add(closureExpression)
        return closureExpression
    }

    class Delegate : ClosureExpressionDescriber {
        override val closureExpressions = mutableListOf<ClosureExpression>()
    }
}