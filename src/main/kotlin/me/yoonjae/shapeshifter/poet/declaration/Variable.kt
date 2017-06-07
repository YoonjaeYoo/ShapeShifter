package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Variable(val name: String, var type: Type? = null, value: String? = null) : Declaration(),
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        ExpressionDescriber by ExpressionDescriber.Delegate(),
        CodeBlockDescriber by CodeBlockDescriber.Delegate() {

    var getterSetterBlock: GetterSetterBlock? = null
    var willSetDidSetBlock: WillSetDidSetBlock? = null

    init {
        value?.let { generalExpression(it) }
    }

    fun getterSetterBlock(init: (GetterSetterBlock.() -> Unit)? = null) {
        val getterSetterBlock = GetterSetterBlock()
        init?.invoke(getterSetterBlock)
        this.getterSetterBlock = getterSetterBlock
    }

    fun willSetDidSetBlock(init: (WillSetDidSetBlock.() -> Unit)? = null) {
        val willSetDidSetBlock = WillSetDidSetBlock()
        init?.invoke(willSetDidSetBlock)
        this.willSetDidSetBlock = willSetDidSetBlock
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("var ")
        writer.write(name)
        type?.let {
            writer.write(": ")
            it.render(writer)
        }
        if (codeBlock != null) {
            writer.write(" ")
            codeBlock!!.render(writer, linePrefix)
        } else if (getterSetterBlock != null) {
            writer.write(" ")
            getterSetterBlock!!.render(writer, linePrefix)
        } else if (willSetDidSetBlock != null) {
            writer.write(" ")
            willSetDidSetBlock!!.render(writer, linePrefix)
        } else if (expressions.isNotEmpty()) {
            writer.write(" = ")
            expressions.first().render(writer, linePrefix)
        }
    }
}

interface VariableDescriber : Describer {

    val variables: MutableList<Variable>

    fun variable(name: String, type: Type? = null, value: String? = null,
                 init: (Variable.() -> Unit)? = null): Variable {
        val variable = Variable(name, type, value)
        init?.invoke(variable)
        variables.add(variable)
        return variable
    }

    class Delegate : VariableDescriber {
        override val variables = mutableListOf<Variable>()
    }
}
