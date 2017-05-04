package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.statement.Statement
import me.yoonjae.shapeshifter.poet.statement.StatementDescriber
import me.yoonjae.shapeshifter.poet.statement.render
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class ClosureExpression : Expression, ClosureParameterDescriber, StatementDescriber {

    override val closureParameters = mutableListOf<ClosureParameter>()
    override val customExpressions = mutableListOf<CustomExpression>()
    override val initializerExpressions = mutableListOf<InitializerExpression>()
    override val closureExpressions = mutableListOf<ClosureExpression>()
    override val expressions = mutableListOf<Expression>()
    override val statements = mutableListOf<Statement>()

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.writeln("{")
        closureParameters.render(writer)
        writer.writeln(" in")
        statements.render(writer, Indent(1) + linePrefix)
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface ClosureExpressionDescriber : Describer {

    val closureExpressions: MutableList<in ClosureExpression>

    fun closureExpression(init: (ClosureExpression.() -> Unit)? = null): ClosureExpression {
        val closureExpression = ClosureExpression()
        init?.invoke(closureExpression)
        closureExpressions.add(closureExpression)
        return closureExpression
    }
}