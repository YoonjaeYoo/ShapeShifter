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
                else -> throw RuntimeException()
            })
        }
    }
}

fun ArgumentDescriber.layoutArguments(element: Element, parent: Element? = null) {
    argument("theme", "theme")
    when (element.tagName) {
        "FrameLayout" -> frameLayoutArguments(element, parent)
        "LinearLayout" -> linearLayoutArguments(element, parent)
        "View" -> viewArguments(element, parent)
        "Button" -> buttonArguments(element, parent)
        "TextView" -> textViewArguments(element, parent)
        "ImageView" -> imageViewArguments(element, parent)
        "com.flaviofaria.kenburnsview.KenBurnsView" -> imageViewArguments(element, parent)
        "include" -> {
        }
        else -> {
            idArgument(element)
            layoutParamsArgument(element, parent)
            println("  ${element.tagName}")
        }
    }
    if (parent == null) {
        argument("config", "config")
    } else {
        element.id()?.let {
            argument("config", it.toConfigParameterName())
        }
    }
}

fun ArgumentDescriber.frameLayoutArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
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
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
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
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
}

fun ArgumentDescriber.buttonArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
    element.attr("android:gravity")?.let {
        argument("gravity", it.toGravity())
    }
    textArgument(element)
    textAppearanceArgument(element)
    textColorArgument(element)
    textSizeArgument(element)
    textStyleArgument(element)
}

fun ArgumentDescriber.textViewArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
    listOf("android:lines", "android:singleLine").forEach { name ->
        element.attr(name)?.let {
            argument(name.substring(8), it)
        }
    }
    textArgument(element)
    textAppearanceArgument(element)
    textColorArgument(element)
    textSizeArgument(element)
    textStyleArgument(element)
}

fun ArgumentDescriber.imageViewArguments(element: Element, parent: Element? = null) {
    idArgument(element)
    layoutParamsArgument(element, parent)
    paddingArgument(element)
    minWidthArgument(element)
    minHeightArgument(element)
    alphaArgument(element)
    backgroundArgument(element)
    element.attr("android:scaleType")?.let {
        argument("scaleType", ".$it")
    }
    element.attr("android:src")?.let {
        argument("src", it.toImage())
    }
}

private fun ArgumentDescriber.layoutParamsArgument(element: Element, parent: Element? = null,
                                                   init: (ArgumentDescriber.() -> Unit)? = null) {
    argument("layoutParams") {
        val prefix = if (parent == null) "Layout" else parent.tagName
        initializerExpression("${prefix}Params") {
            argument("width", element.layoutWidth())
            argument("height", element.layoutHeight())
            element.layoutMargin()?.let {
                argument("margin") {
                    initializerExpression("UIEdgeInsets") {
                        it.forEach { k, v -> argument(k, v) }
                    }
                }
            }
            element.attr("android:layout_gravity")?.let {
                argument("gravity", it.toGravity())
            }
            init?.invoke(this)
        }
    }
}

private fun ArgumentDescriber.idArgument(element: Element) {
    element.id()?.let { argument("id", "\"$it\"") }
}

private fun ArgumentDescriber.paddingArgument(element: Element) {
    element.padding()?.let {
        argument("padding") {
            initializerExpression("UIEdgeInsets") {
                it.forEach { k, v -> argument(k, v) }
            }
        }
    }
}

private fun ArgumentDescriber.minWidthArgument(element: Element) {
    element.attr("android:minWidth")?.let {
        argument("minWidth", it.toDimen())
    }
}

private fun ArgumentDescriber.minHeightArgument(element: Element) {
    element.attr("android:minHeight")?.let {
        argument("minHeight", it.toDimen())
    }
}

private fun ArgumentDescriber.alphaArgument(element: Element) {
    element.attr("android:alpha")?.let {
        argument("alpha", it)
    }
}

private fun ArgumentDescriber.backgroundArgument(element: Element) {
    element.attr("android:background")?.let {
        argument("background", it.toColor())
    }
}

private fun ArgumentDescriber.textArgument(element: Element) {
    element.attr("android:text")?.let {
        val text = if (it.startsWith("@string/")) {
            "\"${it.substring(8)}\".localized()"
        } else {
            "\"$it\""
        }
        argument("text", text)
    }
}

private fun ArgumentDescriber.textAppearanceArgument(element: Element) {
    element.attr("android:textAppearance")?.let {
        if (it.startsWith("@style")) {
            argument("textAppearance", "${it.substring(7)}(theme)")
        } else {
            null
        }
    }
}

private fun ArgumentDescriber.textColorArgument(element: Element) {
    element.attr("android:textColor")?.let {
        argument("textColor", it.toColor())
    }
}

private fun ArgumentDescriber.textSizeArgument(element: Element) {
    element.attr("android:textSize")?.let {
        argument("textSize", it.toDimen())
    }
}

private fun ArgumentDescriber.textStyleArgument(element: Element) {
    element.attr("android:textStyle")?.let {
        it.split("|").let {
            if (it.isNotEmpty()) {
                argument("textStyle", ".${it[0]}")
            }
        }
    }
}
