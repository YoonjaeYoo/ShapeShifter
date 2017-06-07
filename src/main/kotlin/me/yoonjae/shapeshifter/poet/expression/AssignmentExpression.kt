package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import java.io.Writer

class AssignmentExpression(val target: Expression, value: String? = null) : Expression(),
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    constructor(target: String, value: String? = null) : this(GeneralExpression(target), value)

    init {
        value?.let { generalExpression(it) }
    }

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        target.render(writer, Indent(1) + linePrefix)
        writer.write(" = ")
        expressions.first().render(writer, linePrefix)
    }
}

interface AssignmentExpressionDescriber : Describer {

    val assignmentExpressions: MutableList<AssignmentExpression>

    fun assignmentExpression(target: String, value: String? = null,
                             init: (AssignmentExpression.() -> Unit)? = null):
            AssignmentExpression {
        val assignmentExpression = AssignmentExpression(target, value)
        init?.invoke(assignmentExpression)
        assignmentExpressions.add(assignmentExpression)
        return assignmentExpression
    }

    fun assignmentExpression(target: Expression, value: String? = null,
                             init: (AssignmentExpression.() -> Unit)? = null):
            AssignmentExpression {
        val assignmentExpression = AssignmentExpression(target, value)
        init?.invoke(assignmentExpression)
        assignmentExpressions.add(assignmentExpression)
        return assignmentExpression
    }

    class Delegate : AssignmentExpressionDescriber {
        override val assignmentExpressions = mutableListOf<AssignmentExpression>()
    }
}
