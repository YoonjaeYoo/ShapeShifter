package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import java.io.Writer

class GeneralExpression(val value: String) : Expression() {

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        writer.write(value)
    }
}

interface GeneralExpressionDescriber : Describer {

    val generalExpressions: MutableList<GeneralExpression>

    fun generalExpression(value: String, init: (GeneralExpression.() -> Unit)? = null):
            GeneralExpression {
        val generalExpression = GeneralExpression(value)
        init?.invoke(generalExpression)
        generalExpressions.add(generalExpression)
        return generalExpression
    }

    class Delegate : GeneralExpressionDescriber {
        override val generalExpressions = mutableListOf<GeneralExpression>()
    }
}
