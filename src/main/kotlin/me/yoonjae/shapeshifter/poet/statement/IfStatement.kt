package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.CodeBlockDescriber
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import java.io.Writer

class IfStatement(condition: String? = null) : Statement,
        ExpressionDescriber by ExpressionDescriber.Delegate(),
        CodeBlockDescriber by CodeBlockDescriber.Delegate() {

    init {
        condition?.let { generalExpression(it) }
    }

    var elseClause: ElseClause? = null

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("if ")
        expressions.forEachIndexed { index, expression ->
            if (index > 0) writer.write(", ")
            expression.render(writer)
        }
        writer.write(" ")
        codeBlock!!.render(writer, linePrefix)
        elseClause?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
        }
    }

    fun elseClause(init: (ElseClause.() -> Unit)? = null): ElseClause {
        val elseClause = ElseClause()
        init?.invoke(elseClause)
        this.elseClause = elseClause
        return elseClause
    }

    class ElseClause : Element,
            CodeBlockDescriber by CodeBlockDescriber.Delegate() {

        var ifStatement: IfStatement? = null

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.write("else ")
            if (ifStatement == null) {
                codeBlock!!.render(writer, linePrefix)
            } else {
                ifStatement!!.render(writer, linePrefix)
            }
        }

        fun ifStatement(condition: String? = null, init: (IfStatement.() -> Unit)? = null):
                IfStatement {
            val ifStatement = IfStatement(condition)
            init?.invoke(ifStatement)
            this.ifStatement = ifStatement
            return ifStatement
        }
    }
}

interface IfStatementDescriber : Describer {

    val ifStatements: MutableList<IfStatement>

    fun ifStatement(condition: String? = null, init: (IfStatement.() -> Unit)? = null):
            IfStatement {
        val ifStatement = IfStatement(condition)
        init?.invoke(ifStatement)
        ifStatements.add(ifStatement)
        return ifStatement
    }

    class Delegate : IfStatementDescriber {
        override val ifStatements = mutableListOf<IfStatement>()
    }
}
