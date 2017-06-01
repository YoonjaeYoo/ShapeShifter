package me.yoonjae.shapeshifter.translator.extensions

import com.github.javaparser.ast.expr.AnnotationExpr

fun AnnotationExpr.value(): String {
    var value = childNodes[1].toString()
    if (value.contains("value = ")) {
        value = value.replace("value = ", "")
    }
    return value.unquoted()
}

