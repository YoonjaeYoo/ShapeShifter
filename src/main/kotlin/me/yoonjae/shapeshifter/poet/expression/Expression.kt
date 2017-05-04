package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.statement.Statement

interface Expression : Statement

interface ExpressionDescriber : Describer, CustomExpressionDescriber,
        FunctionCallExpressionDescriber, InitializerExpressionDescriber,
        ClosureExpressionDescriber {

    val expressions: MutableList<in Expression>
    override val customExpressions: MutableList<in CustomExpression>
        get() = expressions
    override val functionCallExpressions: MutableList<in FunctionCallExpression>
        get() = expressions
    override val initializerExpressions: MutableList<in InitializerExpression>
        get() = expressions
    override val closureExpressions: MutableList<in ClosureExpression>
        get() = expressions
}
