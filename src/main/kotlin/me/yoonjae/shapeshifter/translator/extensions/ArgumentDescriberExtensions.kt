package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import org.w3c.dom.Element

fun ArgumentDescriber.layoutArguments(element: Element, parent: Element? = null) {
    when (element.tagName) {
        "FrameLayout" -> frameLayoutArguments(element, parent)
        "LinearLayout" -> linearLayoutArguments(element, parent)
        "View" -> viewArguments(element, parent)
        "Button" -> buttonArguments(element, parent)
        "TextView" -> textViewArguments(element, parent)
        "ImageView" -> imageViewArguments(element, parent)
        "com.flaviofaria.kenburnsview.KenBurnsView" -> imageViewArguments(element, parent)
        else -> {
            println("  ${element.tagName}")
        }
    }
}

fun ArgumentDescriber.frameLayoutArguments(element: Element, parent: Element? = null) {
    requiredArguments(element, parent)
    argument("sublayouts") {
        arrayLiteralExpression {
            element.childNodes.elementIterator().forEach {
                layoutExpression(it, element)
            }
        }
    }
}

fun ArgumentDescriber.viewArguments(element: Element, parent: Element? = null) {
    requiredArguments(element, parent)
}

fun ArgumentDescriber.linearLayoutArguments(element: Element, parent: Element? = null) {
    argument("axis") {
        generalExpression(when (element.attr("android:orientation")) {
            "vertical" -> ".vertical"
            else -> ".horizontal"
        })
    }
    requiredArguments(element, parent)
    argument("sublayouts") {
        arrayLiteralExpression {
            element.childNodes.elementIterator().forEach {
                layoutExpression(it, element)
            }
        }
    }
}

fun ArgumentDescriber.buttonArguments(element: Element, parent: Element? = null) {
    requiredArguments(element, parent)
    argument("text", element.text())
}

fun ArgumentDescriber.textViewArguments(element: Element, parent: Element? = null) {
    requiredArguments(element, parent)
    argument("text", element.text())
}

fun ArgumentDescriber.imageViewArguments(element: Element, parent: Element? = null) {
    requiredArguments(element, parent)
}

private fun ArgumentDescriber.requiredArguments(element: Element, parent: Element? = null,
                                                layoutParamsInit: (ArgumentDescriber.() -> Unit)? = null) {
    layoutParamsArgument(element, parent, layoutParamsInit)
    idArgument(element)
    paddingArgument(element)
}

private fun ArgumentDescriber.layoutParamsArgument(element: Element, parent: Element? = null,
                                                   init: (ArgumentDescriber.() -> Unit)? = null) {
    argument("layoutParams") {
        val prefix = if (parent == null) "" else "${parent.tagName}."
        initializerExpression("${prefix}LayoutParams") {
            argument("width", element.width())
            argument("height", element.height())
            argument("margin") {
                initializerExpression("UIEdgeInsets") {
                    element.margin().forEach { k, v -> argument(k, v) }
                }
            }
            init?.invoke(this)
        }
    }
}

private fun ArgumentDescriber.idArgument(element: Element) {
    element.id()?.let { argument("id", "\"$it\"") }
}

private fun ArgumentDescriber.paddingArgument(element: Element) {
    argument("padding") {
        initializerExpression("UIEdgeInsets") {
            element.padding().forEach { k, v -> argument(k, v) }
        }
    }
}
