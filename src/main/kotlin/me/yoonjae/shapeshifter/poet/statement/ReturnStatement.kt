package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import java.io.Writer

class ReturnStatement(val value: String? = null) : Statement(),
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    init {
        value?.let { generalExpression(it) }
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("return")
        if (expressions.isNotEmpty()) {
            writer.write(" ")
            expressions.first().render(writer, linePrefix)
        }
    }
}

interface ReturnStatementDescriber : Describer {

    val returnStatements: MutableList<ReturnStatement>

    fun returnStatement(value: String? = null, init: (ReturnStatement.() -> Unit)? = null):
            ReturnStatement {
        val returnStatement = ReturnStatement(value)
        init?.invoke(returnStatement)
        returnStatements.add(returnStatement)
        return returnStatement
    }

    class Delegate : ReturnStatementDescriber {
        override val returnStatements = mutableListOf<ReturnStatement>()
    }
}