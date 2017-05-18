package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.expression.ClosureExpression
import me.yoonjae.shapeshifter.poet.expression.TrailingClosureDescriber
import org.w3c.dom.Element

fun TrailingClosureDescriber.config(element: Element, parameterName: String,
                                    init: (ClosureExpression.() -> Unit)? = null) {
    trailingClosure {
        closureParameter(parameterName)
        element.alpha()?.let {
            generalExpression("$parameterName.alpha = $it")
        }
        element.backgroundColor()?.let {
            generalExpression("$parameterName.backgroundColor = $it")
        }
        when (element.tagName) {
            "ImageView" -> {
                element.image()?.let {
                    generalExpression("imageView.image = $it")
                }
            }
        }
        init?.invoke(this)
        element.id()?.let {
            generalExpression("${it.toConfigParameterName()}?($parameterName)")
        }
    }
}
