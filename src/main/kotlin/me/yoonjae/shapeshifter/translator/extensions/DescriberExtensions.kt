package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
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
        }
    }
}

fun ExpressionDescriber.imageViewExpression(element: Element) {
    marginLayoutExpression(element) {
        initializerExpression("SizeLayout<UIImageView>") {
            sizeArguments(element)
            alignmentArgument(element)
            trailingClosure {
                closureParameter("imageView")
                element.image()?.let {
                    generalExpression("imageView.image = $it")
                }
            }
        }
    }
}

fun ExpressionDescriber.buttonExpression(element: Element) {
    marginLayoutExpression(element) {
        initializerExpression("ButtonLayout<UIButton>") {
            argument("type", ".system")
            argument("title", element.text())
            alignmentArgument(element)
        }
    }
}

fun ExpressionDescriber.marginLayoutExpression(element: Element,
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

fun ArgumentDescriber.alignmentArgument(element: Element) {
    argument("alignment") {
        initializerExpression("Alignment") {
            argument("vertical", element.verticalAlignment())
            argument("horizontal", element.horizontalAlignment())
        }
    }
}

fun ArgumentDescriber.sizeArguments(element: Element) {
    argument("width", element.width())
    argument("height", element.height())
}
