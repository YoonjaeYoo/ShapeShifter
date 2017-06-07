package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class ExplicitMemberExpression(val expression: Expression, val identifier: String) : Expression() {

    constructor(target: String, identifier: String) : this(GeneralExpression(target), identifier)

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        expression.renderExpression(writer, linePrefix)
        writer.write(".")
        writer.write(identifier)
    }
}

interface ExplicitMemberExpressionDescriber : Describer {

    val explicitMemberExpressions: MutableList<ExplicitMemberExpression>

    fun explicitMemberExpression(expression: String, identifier: String,
                                 init: (ExplicitMemberExpression.() -> Unit)? = null):
            ExplicitMemberExpression {
        val explicitMemberExpression = ExplicitMemberExpression(expression, identifier)
        init?.invoke(explicitMemberExpression)
        explicitMemberExpressions.add(explicitMemberExpression)
        return explicitMemberExpression
    }

    fun explicitMemberExpression(expression: Expression, identifier: String,
                                 init: (ExplicitMemberExpression.() -> Unit)? = null):
            ExplicitMemberExpression {
        val explicitMemberExpression = ExplicitMemberExpression(expression, identifier)
        init?.invoke(explicitMemberExpression)
        explicitMemberExpressions.add(explicitMemberExpression)
        return explicitMemberExpression
    }

    class Delegate : ExplicitMemberExpressionDescriber {
        override val explicitMemberExpressions = mutableListOf<ExplicitMemberExpression>()
    }
}
