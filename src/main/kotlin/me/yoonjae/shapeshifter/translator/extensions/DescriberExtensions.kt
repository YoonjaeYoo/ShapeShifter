package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.*
import org.w3c.dom.Element

fun ExpressionDescriber.layoutExpression(element: Element) {
    when (element.tagName) {
        "FrameLayout" -> frameLayoutExpression(element)
        "LinearLayout" -> linearLayoutExpression(element)
        "View" -> viewExpression(element)
        "Button" -> buttonExpression(element)
        "TextView" -> textViewExpression(element)
        "ImageView" -> imageViewExpression(element)
        "com.flaviofaria.kenburnsview.KenBurnsView" -> imageViewExpression(element)
        else -> {
            generalExpression("nil")
            println("  ${element.tagName}")
        }
    }
}

fun ExpressionDescriber.frameLayoutExpression(element: Element) {
    initializerExpression("FrameLayout<UIView>") {
        sizeArguments(element)
        marginArgument(element)
        alignmentArgument(element)
        argument("sublayouts") {
            arrayLiteralExpression {
                element.childNodes.elementIterator().forEach {
                    layoutExpression(it)
                }
            }
        }
        config(element, "view")
    }
}

fun ExpressionDescriber.linearLayoutExpression(element: Element) {
    initializerExpression("LinearLayout<UIView>") {
        argument("axis") {
            generalExpression(when (element.attr("android:orientation")) {
                "vertical" -> ".vertical"
                else -> ".horizontal"
            })
        }
        sizeArguments(element)
        marginArgument(element)
        alignmentArgument(element)
        argument("sublayouts") {
            arrayLiteralExpression {
                element.childNodes.elementIterator().forEach {
                    layoutExpression(it)
                }
            }
        }
        config(element, "view")
    }
}

fun ExpressionDescriber.viewExpression(element: Element) {
    standardLayoutExpression(element, "UIView") {
        config(element, "view")
    }
}

fun ExpressionDescriber.buttonExpression(element: Element) {
    standardLayoutExpression(element, "UIView") {
        sublayoutArgument {
            val style = element.style()?.let {
                if (it.startsWith("Button.")) it.substring(7).decapitalize() else null
            } ?: "normal"
            functionCallExpression("ButtonLayout.$style") {
                argument("title", element.text())
                config(element, "button")
            }
        }
    }
}

fun ExpressionDescriber.textViewExpression(element: Element) {
    standardLayoutExpression(element, "UIView") {
        sublayoutArgument {
            val style = element.style()?.let {
                if (it.startsWith("Button.")) it.substring(7).decapitalize() else null
            } ?: "normal"
            functionCallExpression("ButtonLayout.$style") {
                argument("title", element.text())
                config(element, "button")
            }
        }
    }
}

fun ExpressionDescriber.imageViewExpression(element: Element) {
    standardLayoutExpression(element, "UIImageView") {
        config(element, "imageView") {
            element.image()?.let {
                generalExpression("imageView.image = $it")
            }
        }
    }
}

private fun TrailingClosureDescriber.config(element: Element, parameterName: String,
                                            init: (ClosureExpression.() -> Unit)? = null) {
    trailingClosure {
        closureParameter(parameterName)
        element.alpha()?.let {
            generalExpression("$parameterName.alpha = $it")
        }
        element.backgroundColor()?.let {
            generalExpression("$parameterName.backgroundColor = $it")
        }
        init?.invoke(this)
        element.id()?.let {
            generalExpression("${it.toConfigParameterName()}?($parameterName)")
        }
    }
}

private fun ExpressionDescriber.standardLayoutExpression(element: Element, view: String,
                                                         init: (InitializerExpression.() -> Unit)? = null) {
    initializerExpression("StandardLayout<$view>") {
        sizeArguments(element)
        marginArgument(element)
        alignmentArgument(element)
        init?.invoke(this)
    }
}

private fun ArgumentDescriber.sublayoutArgument(init: Argument.() -> Unit) {
    argument("sublayout") {
        init.invoke(this)
    }
}

private fun ArgumentDescriber.sizeArguments(element: Element) {
    argument("width", element.width())
    argument("height", element.height())
}

private fun ArgumentDescriber.marginArgument(element: Element) {
    argument("margin") {
        initializerExpression("EdgeInsets") {
            element.margin().forEach { k, v -> argument(k, v) }
        }
    }
}

private fun ArgumentDescriber.alignmentArgument(element: Element) {
    argument("alignment") {
        initializerExpression("Alignment") {
            argument("vertical", element.verticalAlignment())
            argument("horizontal", element.horizontalAlignment())
        }
    }
}
