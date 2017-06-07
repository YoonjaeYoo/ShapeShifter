package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class FunctionCallExpression(val expression: Expression) : Expression(),
        ArgumentDescriber by ArgumentDescriber.Delegate(),
        TrailingClosureDescriber by TrailingClosureDescriber.Delegate() {

    constructor(target: String) : this(GeneralExpression(target))

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        expression.renderExpression(writer, linePrefix)
        arguments.render(writer, linePrefix)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
        }
    }
}

interface FunctionCallExpressionDescriber : Describer {

    val functionCallExpressions: MutableList<FunctionCallExpression>

    fun functionCallExpression(expression: String,
                               init: (FunctionCallExpression.() -> Unit)? = null):
            FunctionCallExpression {
        val functionCallExpression = FunctionCallExpression(expression)
        init?.invoke(functionCallExpression)
        functionCallExpressions.add(functionCallExpression)
        return functionCallExpression
    }

    fun functionCallExpression(expression: Expression,
                               init: (FunctionCallExpression.() -> Unit)? = null):
            FunctionCallExpression {
        val functionCallExpression = FunctionCallExpression(expression)
        init?.invoke(functionCallExpression)
        functionCallExpressions.add(functionCallExpression)
        return functionCallExpression
    }

    class Delegate : FunctionCallExpressionDescriber {
        override val functionCallExpressions = mutableListOf<FunctionCallExpression>()
    }
}
