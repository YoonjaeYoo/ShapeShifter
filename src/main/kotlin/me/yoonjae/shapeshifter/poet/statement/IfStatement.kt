package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.GeneralExpression
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class IfStatement(val condition: Expression) : Statement,
        StatementDescriber by StatementDescriber.Delegate() {

    constructor(condition: String) : this(GeneralExpression(condition))

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("if ")
        condition.render(writer)
        writer.writeln(" {")
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
    }
}

interface IfStatementDescriber : Describer {

    val ifStatements: MutableList<IfStatement>

    fun ifStatement(condition: String, init: (IfStatement.() -> Unit)? = null): IfStatement {
        val ifStatement = IfStatement(condition)
        init?.invoke(ifStatement)
        ifStatements.add(ifStatement)
        return ifStatement
    }

    fun ifStatement(condition: Expression, init: (IfStatement.() -> Unit)? = null): IfStatement {
        val ifStatement = IfStatement(condition)
        init?.invoke(ifStatement)
        ifStatements.add(ifStatement)
        return ifStatement
    }

    class Delegate : IfStatementDescriber {
        override val ifStatements = mutableListOf<IfStatement>()
    }
}

