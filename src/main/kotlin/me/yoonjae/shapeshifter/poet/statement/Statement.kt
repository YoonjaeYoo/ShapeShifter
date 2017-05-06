package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.*
import me.yoonjae.shapeshifter.poet.declaration.Enum
import me.yoonjae.shapeshifter.poet.declaration.Function
import me.yoonjae.shapeshifter.poet.expression.*
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

interface Statement : Element

interface StatementDescriber : ExpressionDescriber, DeclarationDescriber {

    val statements: MutableList<Statement>

    class Delegate : StatementDescriber,
            ExpressionDescriber by ExpressionDescriber.Delegate(),
            DeclarationDescriber by DeclarationDescriber.Delegate() {

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

        override fun import(name: String, init: (Import.() -> Unit)?): Import {
            return super<StatementDescriber>.import(name, init).also { statements.add(it) }
        }

        override fun constant(name: String, value: String, type: String?,
                              init: (Constant.() -> Unit)?): Constant {
            return super<StatementDescriber>.constant(name, value, type, init).
                    also { statements.add(it) }
        }

        override fun variable(name: String, value: String?, type: String?,
                              init: (Variable.() -> Unit)?): Variable {
            return super<StatementDescriber>.variable(name, value, type, init).
                    also { statements.add(it) }
        }

        override fun typeAlias(name: String, type: String, init: (TypeAlias.() -> Unit)?): TypeAlias {
            return super<StatementDescriber>.typeAlias(name, type, init).
                    also { statements.add(it) }
        }

        override fun function(name: String, result: Type?, init: (Function.() -> Unit)?):
                me.yoonjae.shapeshifter.poet.declaration.Function {
            return super<StatementDescriber>.function(name, result, init).
                    also { statements.add(it) }
        }

        override fun initializer(init: (Initializer.() -> Unit)?): Initializer {
            return super<StatementDescriber>.initializer(init).also { statements.add(it) }
        }

        override fun enum(name: String, init: (Enum.() -> Unit)?):
                me.yoonjae.shapeshifter.poet.declaration.Enum {
            return super<StatementDescriber>.enum(name, init).
                    also { statements.add(it) }
        }

        override fun struct(name: String, init: (Struct.() -> Unit)?): Struct {
            return super<StatementDescriber>.struct(name, init).
                    also { statements.add(it) }
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
