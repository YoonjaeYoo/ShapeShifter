package me.yoonjae.shapeshifter.poet.statement

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.expression.GeneralExpression
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class SwitchStatement(var value: Expression? = null) : Statement(),
        ExpressionDescriber by ExpressionDescriber.Delegate() {

    constructor(condition: String) : this(GeneralExpression(condition))

    val cases = mutableListOf<Case>()
    var default: Default? = null

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write("switch ")
        value!!.render(writer, linePrefix)
        writer.writeln(" {")
        cases.forEach {
            (Indent(1) + linePrefix).render(writer)
            it.render(writer, Indent(1) + linePrefix)
            writer.writeln()
        }
        default?.let {
            (Indent(1) + linePrefix).render(writer)
            it.render(writer, Indent(1) + linePrefix)
            writer.writeln()
        }
        linePrefix?.render(writer)
        writer.write(" }")
    }

    fun case(pattern: String, init: (Case.() -> Unit)? = null) {
        val case = Case(pattern)
        init?.invoke(case)
        cases.add(case)
    }

    fun default(init: (Default.() -> Unit)? = null) {
        val default = Default()
        init?.invoke(default)
        this.default = default
    }

    class Case(val pattern: String) : Element, StatementDescriber by StatementDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.write("case ")
            writer.write(pattern)
            writer.writeln(":")
            (Indent(1) + linePrefix).render(writer)
            statements.render(writer, Indent(1) + linePrefix)
        }
    }

    class Default : Element, StatementDescriber by StatementDescriber.Delegate() {

        override fun render(writer: Writer, linePrefix: Element?) {
            writer.writeln("default:")
            (Indent(1) + linePrefix).render(writer)
            statements.render(writer, Indent(1) + linePrefix)
        }
    }
}

interface SwitchStatementDescriber : Describer {

    val switchStatements: MutableList<SwitchStatement>

    fun switchStatement(value: String, init: (SwitchStatement.() -> Unit)? = null):
            SwitchStatement {
        val switchStatement = SwitchStatement(value)
        init?.invoke(switchStatement)
        switchStatements.add(switchStatement)
        return switchStatement
    }

    fun switchStatement(value: Expression? = null, init: (SwitchStatement.() -> Unit)? = null):
            SwitchStatement {
        val switchStatement = SwitchStatement(value)
        init?.invoke(switchStatement)
        switchStatements.add(switchStatement)
        return switchStatement
    }

    class Delegate : SwitchStatementDescriber {
        override val switchStatements = mutableListOf<SwitchStatement>()
    }
}
