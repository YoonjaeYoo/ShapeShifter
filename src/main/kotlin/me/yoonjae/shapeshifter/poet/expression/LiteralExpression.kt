package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer

interface LiteralExpression : Expression

interface LiteralExpressionDescriber : Describer, ArrayLiteralExpressionDescriber,
        DictionaryLiteralExpressionDescriber {

    val literalExpressions: MutableList<LiteralExpression>

    class Delegate : LiteralExpressionDescriber,
            ArrayLiteralExpressionDescriber by ArrayLiteralExpressionDescriber.Delegate(),
            DictionaryLiteralExpressionDescriber by DictionaryLiteralExpressionDescriber.Delegate() {
        override val literalExpressions = mutableListOf<LiteralExpression>()

        override fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)?):
                ArrayLiteralExpression {
            return super<LiteralExpressionDescriber>.arrayLiteralExpression(init).
                    also { literalExpressions.add(it) }
        }

        override fun dictionaryLiteralExpression(init: (DictionaryLiteralExpression.() -> Unit)?):
                DictionaryLiteralExpression {
            return super<LiteralExpressionDescriber>.dictionaryLiteralExpression(init).
                    also { literalExpressions.add(it) }
        }
    }
}