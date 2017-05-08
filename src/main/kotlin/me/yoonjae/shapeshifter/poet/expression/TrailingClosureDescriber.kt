package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer

interface TrailingClosureDescriber : Describer {

    var trailingClosure: ClosureExpression?

    fun trailingClosure(init: (ClosureExpression.() -> Unit)? = null): ClosureExpression {
        val closureExpression = ClosureExpression()
        init?.invoke(closureExpression)
        trailingClosure = closureExpression
        return closureExpression
    }

    class Delegate : TrailingClosureDescriber {
        override var trailingClosure: ClosureExpression? = null
    }
}