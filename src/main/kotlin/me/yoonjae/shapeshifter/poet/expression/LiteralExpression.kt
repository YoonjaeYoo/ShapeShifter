package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer

interface LiteralExpression : Expression

interface LiteralExpressionDescriber : Describer, ArrayLiteralExpressionDescriber {

    val literalExpressions: MutableList<ArrayLiteralExpression>

    class Delegate : LiteralExpressionDescriber,
            ArrayLiteralExpressionDescriber by ArrayLiteralExpressionDescriber.Delegate() {
        override val literalExpressions = mutableListOf<ArrayLiteralExpression>()

        override fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)?):
                ArrayLiteralExpression {
            return super<LiteralExpressionDescriber>.arrayLiteralExpression(init).
                    also { literalExpressions.add(it) }
        }
    }
}