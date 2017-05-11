package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.declaration.ConstantDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile

val font = SwiftFile("Font.swift") {
    import("UIKit")
    struct("Font") {
        font("body1", 14)
        font("body2", 14)
        font("button", 14)
        font("caption", 12)
        font("display1", 34)
        font("display2", 45)
        font("display3", 56)
        font("display4", 112)
        font("headline", 24)
        font("large", 22)
        font("medium", 18)
        font("small", 14)
        font("subhead", 16)
        font("title", 20)
    }
}

private fun ConstantDescriber.font(name: String, size: Int) {
    constant(name) {
        static()
        generalExpression("UIFont.systemFont(ofSize: $size)")
    }
}
