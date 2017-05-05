package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class FunctionCallExpression(val target: Expression,
                             var trailingClosure: ClosureExpression? = null) :
        Expression, ArgumentDescriber {

    override val arguments = mutableListOf<Argument>()

    override fun render(writer: Writer, linePrefix: Element?) {
        target.render(writer, linePrefix)
        arguments.render(writer, linePrefix)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
        }
    }
}

interface FunctionCallExpressionDescriber : Describer {

    val functionCallExpressions: MutableList<FunctionCallExpression>

    fun functionCallExpression(target: String,
                               trailingClosure: ClosureExpression? = null,
                               init: (FunctionCallExpression.() -> Unit)? = null):
            FunctionCallExpression {
        val functionCallExpression = FunctionCallExpression(GeneralExpression(target),
                trailingClosure)
        init?.invoke(functionCallExpression)
        functionCallExpressions.add(functionCallExpression)
        return functionCallExpression
    }

    fun functionCallExpression(target: Expression,
                               trailingClosure: ClosureExpression? = null,
                               init: (FunctionCallExpression.() -> Unit)? = null):
            FunctionCallExpression {
        val functionCallExpression = FunctionCallExpression(target, trailingClosure)
        init?.invoke(functionCallExpression)
        functionCallExpressions.add(functionCallExpression)
        return functionCallExpression
    }

    class Delegate : FunctionCallExpressionDescriber {
        override val functionCallExpressions = mutableListOf<FunctionCallExpression>()
    }
}
