package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class InitializerExpression(val expression: Expression) : Expression(),
        ArgumentDescriber by ArgumentDescriber.Delegate(),
        TrailingClosureDescriber by TrailingClosureDescriber.Delegate() {

    constructor(target: String) : this(GeneralExpression(target))

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        expression.renderExpression(writer, linePrefix)
        writer.write(".init")
        arguments.render(writer, linePrefix)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
        }
    }
}

interface InitializerExpressionDescriber : Describer {

    val initializerExpressions: MutableList<InitializerExpression>

    fun initializerExpression(expression: String, init: (InitializerExpression.() -> Unit)? = null):
            InitializerExpression {
        val initializerExpression = InitializerExpression(expression)
        init?.invoke(initializerExpression)
        initializerExpressions.add(initializerExpression)
        return initializerExpression
    }

    fun initializerExpression(expression: Expression, init: (InitializerExpression.() -> Unit)? = null):
            InitializerExpression {
        val initializerExpression = InitializerExpression(expression)
        init?.invoke(initializerExpression)
        initializerExpressions.add(initializerExpression)
        return initializerExpression
    }

    class Delegate : InitializerExpressionDescriber {
        override val initializerExpressions = mutableListOf<InitializerExpression>()
    }
}
