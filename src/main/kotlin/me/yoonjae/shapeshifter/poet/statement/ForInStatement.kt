package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.GeneralExpression
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class ForInStatement(var pattern: String, var expression: Expression) : Statement(),
        StatementDescriber by StatementDescriber.Delegate() {

    constructor(pattern: String, expression: String) : this(pattern, GeneralExpression(expression))

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("for ")
        writer.write(pattern)
        writer.write(" in ")
        expression.renderExpression(writer, Indent(1) + linePrefix)
        writer.writeln(" {")
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface ForInStatementDescriber : Describer {

    val forInStatements: MutableList<ForInStatement>

    fun forInStatement(pattern: String, expression: String,
                       init: (ForInStatement.() -> Unit)? = null): ForInStatement {
        val forInStatement = ForInStatement(pattern, expression)
        init?.invoke(forInStatement)
        forInStatements.add(forInStatement)
        return forInStatement
    }

    fun forInStatement(pattern: String, expression: Expression,
                       init: (ForInStatement.() -> Unit)? = null): ForInStatement {
        val forInStatement = ForInStatement(pattern, expression)
        init?.invoke(forInStatement)
        forInStatements.add(forInStatement)
        return forInStatement
    }

    class Delegate : ForInStatementDescriber {
        override val forInStatements = mutableListOf<ForInStatement>()
    }
}
