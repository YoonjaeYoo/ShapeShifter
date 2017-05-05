package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.*
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

interface Statement : Element

interface StatementDescriber : ExpressionDescriber {

    val statements: MutableList<Statement>

    class Delegate : StatementDescriber,
            ExpressionDescriber by ExpressionDescriber.Delegate() {

        override val statements = mutableListOf<Statement>()

        override fun generalExpression(value: String, init: (GeneralExpression.() -> Unit)?):
                GeneralExpression {
            return super<StatementDescriber>.generalExpression(value, init).
                    also { statements.add(it) }
        }

        override fun functionCallExpression(target: String, trailingClosure: ClosureExpression?,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<StatementDescriber>.functionCallExpression(target, trailingClosure, init).
                    also { statements.add(it) }
        }

        override fun functionCallExpression(target: Expression, trailingClosure: ClosureExpression?,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<StatementDescriber>.functionCallExpression(target, trailingClosure, init).
                    also { statements.add(it) }
        }

        override fun initializerExpression(target: String, trailingClosure: ClosureExpression?,
                                           init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<StatementDescriber>.initializerExpression(target, trailingClosure, init).
                    also { statements.add(it) }
        }

        override fun initializerExpression(target: Expression, trailingClosure: ClosureExpression?,
                                           init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<StatementDescriber>.initializerExpression(target, trailingClosure, init).
                    also { statements.add(it) }
        }

        override fun closureExpression(init: (ClosureExpression.() -> Unit)?): ClosureExpression {
            return super<StatementDescriber>.closureExpression(init).also { statements.add(it) }
        }
    }
}

fun List<Statement>.render(writer: Writer, linePrefix: Element? = null) {
    forEach {
        linePrefix?.render(writer)
        it.render(writer, linePrefix)
        writer.writeln()
    }
}
