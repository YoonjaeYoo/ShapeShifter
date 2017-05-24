package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import org.w3c.dom.Element

fun ExpressionDescriber.layoutExpression(element: Element, parent: Element? = null) {
    if (parent == null) {
        initializerExpression("super") {
            layoutArguments(element, parent)
        }
    } else {
        if (element.tagName == "include") {
            element.attr("layout")?.let {
                // @layout/progress_bar_circular -> ProgressBarCircularLayout
                initializerExpression("${it.substring(8).toResourceName(true)}Layout") {
                    layoutArguments(element, parent)
                }
            }
        } else {
            element.style().also {
                val layoutType = element.layoutType
                if (it != null && it.startsWith("$layoutType.")) {
                    functionCallExpression(it) {
                        layoutArguments(element, parent)
                    }
                } else {
                    initializerExpression(layoutType) {
                        layoutArguments(element, parent)
                    }
                }
            }
        }
    }
}
