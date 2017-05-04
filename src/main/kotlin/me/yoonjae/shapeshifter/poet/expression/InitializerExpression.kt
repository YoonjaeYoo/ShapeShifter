package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import java.io.Writer

class InitializerExpression(val target: Expression,
                            var trailingClosure: ClosureExpression? = null) :
        Expression, ArgumentDescriber {

    override val arguments = mutableListOf<Argument>()

    override fun render(writer: Writer, linePrefix: Element?) {
        target.render(writer, linePrefix)
        writer.write(".init")
        arguments.render(writer, linePrefix)
        trailingClosure?.let {
            writer.write(" ")
            it.render(writer, linePrefix)
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
