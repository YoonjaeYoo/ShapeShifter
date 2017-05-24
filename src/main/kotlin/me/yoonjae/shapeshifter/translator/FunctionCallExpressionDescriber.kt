package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.expression.FunctionCallExpressionDescriber

fun FunctionCallExpressionDescriber.increasedToMinSize() {
    functionCallExpression("size.increasedToSize") {
        argument {
            initializerExpression("CGSize") {
                argument("width", "(minWidth ?? 0) + layoutParams.margin.left" +
                        " + layoutParams.margin.right")
                argument("height", "(minHeight ?? 0) + layoutParams.margin.top" +
                        " + layoutParams.margin.bottom")
            }
        }
    }
}
