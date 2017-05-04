package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class InitializerExpression(val target: Expression,
                            val trailingClosure: ClosureExpression? = null) :
        Expression, ArgumentDescriber {

    override val arguments = mutableListOf<Argument>()

    override fun render(writer: Writer, linePrefix: Element?) {
        target.render(writer)
        writer.write(".init")
        arguments.render(writer)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer)
        }
    }
}

interface InitializerExpressionDescriber : Describer {

    val initializerExpressions: MutableList<in InitializerExpression>

    fun initializerExpression(target: String, trailingClosure: ClosureExpression? = null,
                              init: (InitializerExpression.() -> Unit)? = null):
            InitializerExpression {
        return initializerExpression(CustomExpression(target), trailingClosure, init)
    }

    fun initializerExpression(target: Expression, trailingClosure: ClosureExpression? = null,
                              init: (InitializerExpression.() -> Unit)? = null):
            InitializerExpression {
        val expression = InitializerExpression(target, trailingClosure)
        init?.invoke(expression)
        initializerExpressions.add(expression)
        return expression
    }
}
