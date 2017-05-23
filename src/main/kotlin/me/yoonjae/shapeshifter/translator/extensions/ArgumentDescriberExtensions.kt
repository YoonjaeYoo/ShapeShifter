package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.translator.system.themeFields
import org.w3c.dom.Element

fun ArgumentDescriber.themeArguments(element: Element) {
    themeFields.forEach { name, type ->
        element.childNodes.elements().asSequence().find {
            it.tagName == "item" && it.attr("name")?.endsWith(name)!!
        }?.textContent?.let {
            argument(name, when (type.name) {
                "UIColor" -> it.toColor()
                "Drawable" -> it.toDrawable()
                else -> it
            })
        }
    }
}

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
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    argument("sublayouts") {
        arrayLiteralExpression {
            element.childNodes.elements().forEach {
                layoutExpression(it, element)
            }
        }
    }
}

fun ArgumentDescriber.linearLayoutArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    argument("orientation") {
        generalExpression(when (element.attr("android:orientation")) {
            "vertical" -> ".vertical"
            else -> ".horizontal"
        })
    }
    paddingArgument(element)
    argument("sublayouts") {
        arrayLiteralExpression {
            element.childNodes.elements().forEach {
                layoutExpression(it, element)
            }
        }
    }
}

fun ArgumentDescriber.viewArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
}

fun ArgumentDescriber.buttonArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    argument("text", element.text())
}

fun ArgumentDescriber.textViewArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    argument("text", element.text())
}

fun ArgumentDescriber.imageViewArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
}

private fun ArgumentDescriber.layoutParamsArgument(element: Element, parent: Element? = null,
                                                   init: (ArgumentDescriber.() -> Unit)? = null) {
    argument("layoutParams") {
        val prefix = if (parent == null) "Layout" else parent.tagName
        initializerExpression("${prefix}Params") {
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
