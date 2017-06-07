package me.yoonjae.shapeshifter.poet.expression

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class DictionaryLiteralExpression : LiteralExpression() {

    class KeyValue(val keyExpression: Expression, val valueExpression: Expression) : Element {
        constructor(key: String, value: String) :
                this(GeneralExpression(key), GeneralExpression(value))

        override fun render(writer: Writer, linePrefix: Element?) {
            keyExpression.render(writer, linePrefix)
            writer.write(": ")
            valueExpression.render(writer, linePrefix)
        }
    }

    val keyValues = mutableListOf<KeyValue>()

    fun keyValue(keyExpression: Expression, valueExpression: Expression,
                 init: (KeyValue.() -> Unit)? = null): KeyValue {
        val keyValue = KeyValue(keyExpression, valueExpression)
        init?.invoke(keyValue)
        keyValues.add(keyValue)
        return keyValue
    }

    fun keyValue(key: String, value: String, init: (KeyValue.() -> Unit)? = null): KeyValue {
        val keyValue = KeyValue(key, value)
        init?.invoke(keyValue)
        keyValues.add(keyValue)
        return keyValue
    }

    override fun renderExpression(writer: Writer, linePrefix: Element?) {
        writer.write("[")
        if (keyValues.isEmpty()) {
            writer.write(":")
        } else if (keyValues.size == 1) {
            keyValues[0].render(writer, linePrefix)
        } else {
            writer.writeln()
            keyValues.forEachIndexed { index, keyValue ->
                if (index > 0) writer.writeln(",")
                (Indent(2) + linePrefix).render(writer)
                keyValue.render(writer, (Indent(2) + linePrefix))
            }
            writer.writeln()
            linePrefix?.render(writer)
        }
        writer.write("]")
    }
}

interface DictionaryLiteralExpressionDescriber : Describer {

    val dictionaryLiteralExpressions: MutableList<DictionaryLiteralExpression>

    fun dictionaryLiteralExpression(init: (DictionaryLiteralExpression.() -> Unit)? = null):
            DictionaryLiteralExpression {
        val dictionaryLiteralExpression = DictionaryLiteralExpression()
        init?.invoke(dictionaryLiteralExpression)
        dictionaryLiteralExpressions.add(dictionaryLiteralExpression)
        return dictionaryLiteralExpression
    }

    class Delegate : DictionaryLiteralExpressionDescriber {
        override val dictionaryLiteralExpressions = mutableListOf<DictionaryLiteralExpression>()
    }
}