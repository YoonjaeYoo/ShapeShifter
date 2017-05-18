package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import org.w3c.dom.Element

fun ExpressionDescriber.layoutExpression(element: Element, parent: Element? = null) {
    if (parent == null) {
        initializerExpression("super") {
            layoutArguments(element, parent)
            config(element, "view")
        }
    } else {
        element.style().also { val layoutType = element.layoutType
            if (it != null && it.startsWith("$layoutType.")) {
                val style = it.substring(layoutType.length + 1).decapitalize()
                functionCallExpression("$layoutType.$style") {
                    layoutArguments(element, parent)
                    config(element, "view")
                }
            } else {
                initializerExpression(layoutType) {
                    layoutArguments(element, parent)
                    config(element, "view")
                }
            }
        }
    }
}
