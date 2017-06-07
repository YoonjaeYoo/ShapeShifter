package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.statement.Statement
import java.io.Writer

abstract class Expression : Statement() {
    var chainingExpression: Expression? = null

    fun initializerExpression(init: (InitializerExpression.() -> Unit)?): InitializerExpression {
        val expression = InitializerExpression(this)
        init?.invoke(expression)
        this.chainingExpression = expression
        return expression
    }

    fun functionCallExpression(init: (FunctionCallExpression.() -> Unit)?): FunctionCallExpression {
        val expression = FunctionCallExpression(this)
        init?.invoke(expression)
        this.chainingExpression = expression
        return expression
    }

    fun explicitMemberExpression(identifier: String, init: (ExplicitMemberExpression.() -> Unit)?):
            ExplicitMemberExpression {
        val expression = ExplicitMemberExpression(this, identifier)
        init?.invoke(expression)
        this.chainingExpression = expression
        return expression
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        if (chainingExpression == null) {
            renderExpression(writer, linePrefix)
        } else {
            chainingExpression?.render(writer, linePrefix)
        }
    }

    abstract fun renderExpression(writer: Writer, linePrefix: Element?)
}

interface ExpressionDescriber : Describer, GeneralExpressionDescriber,
        FunctionCallExpressionDescriber, InitializerExpressionDescriber,
        ClosureExpressionDescriber, LiteralExpressionDescriber, AssignmentExpressionDescriber,
        ExplicitMemberExpressionDescriber {

    val expressions: MutableList<Expression>

    class Delegate : ExpressionDescriber,
            GeneralExpressionDescriber by GeneralExpressionDescriber.Delegate(),
            FunctionCallExpressionDescriber by FunctionCallExpressionDescriber.Delegate(),
            InitializerExpressionDescriber by InitializerExpressionDescriber.Delegate(),
            ClosureExpressionDescriber by ClosureExpressionDescriber.Delegate(),
            LiteralExpressionDescriber by LiteralExpressionDescriber.Delegate(),
            AssignmentExpressionDescriber by AssignmentExpressionDescriber.Delegate(),
            ExplicitMemberExpressionDescriber by ExplicitMemberExpressionDescriber.Delegate() {

        override val expressions = mutableListOf<Expression>()

        override fun generalExpression(value: String, init: (GeneralExpression.() -> Unit)?):
                GeneralExpression {
            return super<ExpressionDescriber>.generalExpression(value, init).
                    also { expressions.add(it) }
        }

        override fun functionCallExpression(expression: String,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<ExpressionDescriber>.functionCallExpression(expression, init).
                    also { expressions.add(it) }
        }

        override fun functionCallExpression(expression: Expression,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<ExpressionDescriber>.functionCallExpression(expression, init).
                    also { expressions.add(it) }
        }

        override fun initializerExpression(expression: String, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<ExpressionDescriber>.initializerExpression(expression, init).
                    also { expressions.add(it) }
        }

        override fun initializerExpression(expression: Expression, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<ExpressionDescriber>.initializerExpression(expression, init).
                    also { expressions.add(it) }
        }

        override fun closureExpression(init: (ClosureExpression.() -> Unit)?): ClosureExpression {
            return super<ExpressionDescriber>.closureExpression(init).also { expressions.add(it) }
        }

        override fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)?):
                ArrayLiteralExpression {
            return super<ExpressionDescriber>.arrayLiteralExpression(init).
                    also { expressions.add(it) }
        }

        override fun dictionaryLiteralExpression(init: (DictionaryLiteralExpression.() -> Unit)?):
                DictionaryLiteralExpression {
            return super<ExpressionDescriber>.dictionaryLiteralExpression(init).
                    also { expressions.add(it) }
        }

        override fun assignmentExpression(target: String, value: String?,
                                          init: (AssignmentExpression.() -> Unit)?):
                AssignmentExpression {
            return super<ExpressionDescriber>.assignmentExpression(target, value, init).
                    also { expressions.add(it) }
        }

        override fun assignmentExpression(target: Expression, value: String?,
                                          init: (AssignmentExpression.() -> Unit)?):
                AssignmentExpression {
            return super<ExpressionDescriber>.assignmentExpression(target, value, init).
                    also { expressions.add(it) }
        }

        override fun explicitMemberExpression(expression: String, identifier: String,
                                              init: (ExplicitMemberExpression.() -> Unit)?):
                ExplicitMemberExpression {
            return super<ExpressionDescriber>.explicitMemberExpression(expression, identifier, init)
        }

        override fun explicitMemberExpression(expression: Expression, identifier: String,
                                              init: (ExplicitMemberExpression.() -> Unit)?):
                ExplicitMemberExpression {
            return super<ExpressionDescriber>.explicitMemberExpression(expression, identifier, init)
        }
    }
}
