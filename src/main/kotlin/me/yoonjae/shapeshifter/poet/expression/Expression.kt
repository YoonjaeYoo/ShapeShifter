package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.statement.Statement

interface Expression : Statement

interface ExpressionDescriber : Describer, GeneralExpressionDescriber,
        FunctionCallExpressionDescriber, InitializerExpressionDescriber,
        ClosureExpressionDescriber, LiteralExpressionDescriber {

    val expressions: MutableList<Expression>

    class Delegate : ExpressionDescriber,
            GeneralExpressionDescriber by GeneralExpressionDescriber.Delegate(),
            FunctionCallExpressionDescriber by FunctionCallExpressionDescriber.Delegate(),
            InitializerExpressionDescriber by InitializerExpressionDescriber.Delegate(),
            ClosureExpressionDescriber by ClosureExpressionDescriber.Delegate(),
            LiteralExpressionDescriber by LiteralExpressionDescriber.Delegate() {

        override val expressions = mutableListOf<Expression>()

        override fun generalExpression(value: String, init: (GeneralExpression.() -> Unit)?):
                GeneralExpression {
            return super<ExpressionDescriber>.generalExpression(value, init).
                    also { expressions.add(it) }
        }

        override fun functionCallExpression(target: String,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<ExpressionDescriber>.functionCallExpression(target, init).
                    also { expressions.add(it) }
        }

        override fun functionCallExpression(target: Expression,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<ExpressionDescriber>.functionCallExpression(target, init).
                    also { expressions.add(it) }
        }

        override fun initializerExpression(target: String, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<ExpressionDescriber>.initializerExpression(target, init).
                    also { expressions.add(it) }
        }

        override fun initializerExpression(target: Expression, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<ExpressionDescriber>.initializerExpression(target, init).
                    also { expressions.add(it) }
        }

        override fun closureExpression(init: (ClosureExpression.() -> Unit)?): ClosureExpression {
            return super<ExpressionDescriber>.closureExpression(init).also { expressions.add(it) }
        }

        override fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)?): ArrayLiteralExpression {
            return super<ExpressionDescriber>.arrayLiteralExpression(init).
                    also { expressions.add(it) }
        }
    }
}
