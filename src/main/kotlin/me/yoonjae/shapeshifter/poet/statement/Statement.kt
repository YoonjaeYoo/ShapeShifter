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

interface StatementDescriber : ExpressionDescriber, DeclarationDescriber,
        ReturnStatementDescriber, IfStatementDescriber {

    val statements: MutableList<Statement>

    class Delegate : StatementDescriber,
            ExpressionDescriber by ExpressionDescriber.Delegate(),
            DeclarationDescriber by DeclarationDescriber.Delegate(),
            ReturnStatementDescriber by ReturnStatementDescriber.Delegate(),
            IfStatementDescriber by IfStatementDescriber.Delegate() {

        override val statements = mutableListOf<Statement>()

        override fun generalExpression(value: String, init: (GeneralExpression.() -> Unit)?):
                GeneralExpression {
            return super<StatementDescriber>.generalExpression(value, init).
                    also { statements.add(it) }
        }

        override fun functionCallExpression(target: String,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<StatementDescriber>.functionCallExpression(target, init).
                    also { statements.add(it) }
        }

        override fun functionCallExpression(target: Expression,
                                            init: (FunctionCallExpression.() -> Unit)?):
                FunctionCallExpression {
            return super<StatementDescriber>.functionCallExpression(target, init).
                    also { statements.add(it) }
        }

        override fun initializerExpression(target: String, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<StatementDescriber>.initializerExpression(target, init).
                    also { statements.add(it) }
        }

        override fun initializerExpression(target: Expression, init: (InitializerExpression.() -> Unit)?):
                InitializerExpression {
            return super<StatementDescriber>.initializerExpression(target, init).
                    also { statements.add(it) }
        }

        override fun closureExpression(init: (ClosureExpression.() -> Unit)?): ClosureExpression {
            return super<StatementDescriber>.closureExpression(init).also { statements.add(it) }
        }

        override fun arrayLiteralExpression(init: (ArrayLiteralExpression.() -> Unit)?):
                ArrayLiteralExpression {
            return super<StatementDescriber>.arrayLiteralExpression(init).
                    also { statements.add(it) }
        }

        override fun dictionaryLiteralExpression(init: (DictionaryLiteralExpression.() -> Unit)?):
                DictionaryLiteralExpression {
            return super<StatementDescriber>.dictionaryLiteralExpression(init).
                    also { expressions.add(it) }
        }

        override fun assignmentExpression(target: String, value: String?,
                                          init: (AssignmentExpression.() -> Unit)?):
                AssignmentExpression {
            return super<StatementDescriber>.assignmentExpression(target, value, init).
                    also { statements.add(it) }
        }

        override fun assignmentExpression(target: Expression, value: String?,
                                          init: (AssignmentExpression.() -> Unit)?):
                AssignmentExpression {
            return super<StatementDescriber>.assignmentExpression(target, value, init).
                    also { statements.add(it) }
        }

        override fun import(name: String, init: (Import.() -> Unit)?): Import {
            return super<StatementDescriber>.import(name, init).also { statements.add(it) }
        }

        override fun constant(name: String, type: Type?, value: String?,
                              init: (Constant.() -> Unit)?): Constant {
            return super<StatementDescriber>.constant(name, type, value, init).
                    also { statements.add(it) }
        }

        override fun variable(name: String, type: Type?, value: String?,
                              init: (Variable.() -> Unit)?): Variable {
            return super<StatementDescriber>.variable(name, type, value, init).
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

        override fun initializer(optional: Boolean, init: (Initializer.() -> Unit)?): Initializer {
            return super<StatementDescriber>.initializer(optional, init).also { statements.add(it) }
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

        override fun clazz(name: String, init: (Class.() -> Unit)?): Class {
            return super<StatementDescriber>.clazz(name, init).
                    also { statements.add(it) }
        }

        override fun protocol(name: String, init: (Protocol.() -> Unit)?): Protocol {
            return super<StatementDescriber>.protocol(name, init).
                    also { statements.add(it) }
        }

        override fun returnStatement(value: String?, init: (ReturnStatement.() -> Unit)?): ReturnStatement {
            return super<StatementDescriber>.returnStatement(value, init).
                    also { statements.add(it) }
        }

        override fun ifStatement(condition: String?, init: (IfStatement.() -> Unit)?): IfStatement {
            return super<StatementDescriber>.ifStatement(condition, init).
                    also { statements.add(it) }
        }
    }
}

fun List<Statement>.render(writer: Writer, linePrefix: Element? = null) {
    forEachIndexed { index, statement ->
        if (index > 0) {
            writer.writeln()
            linePrefix?.render(writer)
        }
        statement.render(writer, linePrefix)
    }
}
