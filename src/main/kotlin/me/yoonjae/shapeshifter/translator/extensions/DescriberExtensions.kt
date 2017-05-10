package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.poet.expression.ClosureExpression
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.expression.TrailingClosureDescriber
import org.w3c.dom.Element

fun ExpressionDescriber.layoutExpression(element: Element) {
    when (element.tagName) {
        "FrameLayout" -> frameLayoutExpression(element)
        "ImageView" -> imageViewExpression(element)
        "Button" -> buttonExpression(element)
        else -> generalExpression("nil")
    }
}

fun ExpressionDescriber.frameLayoutExpression(element: Element) {
    marginLayoutExpression(element) {
        initializerExpression("PileLayout") {
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
}

fun ExpressionDescriber.imageViewExpression(element: Element) {
    marginLayoutExpression(element) {
        initializerExpression("SizeLayout<UIImageView>") {
            sizeArguments(element)
            alignmentArgument(element)
            config(element, "imageView") {
                element.image()?.let {
                    generalExpression("imageView.image = $it")
                }
            }
        }
    }
}

fun ExpressionDescriber.buttonExpression(element: Element) {
    marginLayoutExpression(element) {
        val style = element.style()?.let {
            if (it.startsWith("Button.")) it.substring(7).decapitalize() else null
        } ?: "normal"
        functionCallExpression("ButtonLayout.$style") {
            argument("title", element.text())
            alignmentArgument(element)
            config(element, "button")
        }
    }
}

private fun TrailingClosureDescriber.config(element: Element, parameterName: String,
                                            init: (ClosureExpression.() -> Unit)? = null) {
    val id = element.id()
    if (id != null || init != null) {
        trailingClosure {
            closureParameter(parameterName)
            init?.invoke(this)
            element.id()?.let {
                generalExpression("${it.toConfigParameterName()}?($parameterName)")
            }
        }
    }
}

private fun ExpressionDescriber.marginLayoutExpression(element: Element,
                                                       sublayout: (ExpressionDescriber.() -> Unit)) {
    initializerExpression("InsetLayout") {
        argument("insets") {
            initializerExpression("EdgeInsets") {
                element.insets().forEach { k, v -> argument(k, v) }
            }
        }
        argument("sublayout") {
            sublayout.invoke(this)
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

private fun ArgumentDescriber.sizeArguments(element: Element) {
    element.width().let {
        argument("minWidth", it)
        argument("maxWidth", it)
    }
    element.height().let {
        argument("minHeight", it)
        argument("maxHeight", it)
    }
}
