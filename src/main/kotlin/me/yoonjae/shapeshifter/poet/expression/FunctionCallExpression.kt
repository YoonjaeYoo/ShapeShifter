package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class FunctionCallExpression(val target: Expression,
                             val trailingClosure: ClosureExpression? = null) :
        Expression, ArgumentDescriber {

    override val arguments = mutableListOf<Argument>()

    override fun render(writer: Writer, linePrefix: Element?) {
        target.render(writer)
        arguments.render(writer)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer)
        }
    }
}

interface FunctionCallExpressionDescriber : Describer {

    val functionCallExpressions: MutableList<in FunctionCallExpression>

    fun functionCallExpression(target: String,
                               trailingClosure: ClosureExpression? = null,
                               init: (FunctionCallExpression.() -> Unit)? = null):
            FunctionCallExpression {
        return functionCallExpression(CustomExpression(target), trailingClosure, init)
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
}