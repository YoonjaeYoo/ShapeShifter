package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class CustomExpression(val value: String) : Expression {

    override fun render(writer: Writer, linePrefix: Element?) {
        writer.write(value)
    }
}

interface CustomExpressionDescriber : Describer {

    val customExpressions: MutableList<in CustomExpression>

    fun customExpression(value: String, init: (CustomExpression.() -> Unit)? = null):
            CustomExpression {
        val customExpression = CustomExpression(value)
        init?.invoke(customExpression)
        customExpressions.add(customExpression)
        return customExpression
    }
}