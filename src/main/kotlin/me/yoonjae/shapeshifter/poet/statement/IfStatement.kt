package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.GeneralExpression
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

open class IfStatement(var condition: Expression) : Statement(),
        StatementDescriber by StatementDescriber.Delegate() {

    constructor(condition: String) : this(GeneralExpression(condition))

    var elseClause: ElseClause? = null
        set(value) {
            field = value
            if( value != null) {
                elseIfStatement = null
            }
        }
    var elseIfStatement: ElseIfStatement? = null
        set(value) {
            field = value
            if( value != null) {
                elseClause = null
            }
        }

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("if ")
        condition!!.render(writer, linePrefix)
        writer.writeln(" {")
        (Indent(1) + linePrefix).render(writer)
        statements.render(writer, Indent(1) + linePrefix)
        writer.writeln()
        linePrefix?.render(writer)
        writer.write("}")
        elseClause?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
        }
        elseIfStatement?.let {
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

    fun elseIfStatement(condition: String, init: (ElseIfStatement.() -> Unit)? = null):
            ElseIfStatement {
        val elseIfStatement = ElseIfStatement(condition)
        init?.invoke(elseIfStatement)
        this.elseIfStatement = elseIfStatement
        return elseIfStatement
    }

    fun elseIfStatement(condition: Expression, init: (ElseIfStatement.() -> Unit)? = null):
            ElseIfStatement {
        val elseElseIfClause = ElseIfStatement(condition)
        init?.invoke(elseElseIfClause)
        this.elseIfStatement = elseIfStatement
        return elseElseIfClause
    }

    class ElseClause : Element,
            StatementDescriber by StatementDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.writeln("else {")
            (Indent(1) + linePrefix).render(writer)
            statements.render(writer, Indent(1) + linePrefix)
            writer.writeln()
            linePrefix?.render(writer)
            writer.write("}")
        }
    }

    class ElseIfStatement(condition: Expression) : IfStatement(condition) {

        constructor(condition: String) : this(GeneralExpression(condition))

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.write("else ")
            super.render(writer, linePrefix)
        }
    }
}

interface IfStatementDescriber : Describer {

    val ifStatements: MutableList<IfStatement>

    fun ifStatement(condition: String, init: (IfStatement.() -> Unit)? = null):
            IfStatement {
        val ifStatement = IfStatement(condition)
        init?.invoke(ifStatement)
        ifStatements.add(ifStatement)
        return ifStatement
    }

    fun ifStatement(condition: Expression, init: (IfStatement.() -> Unit)? = null):
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
