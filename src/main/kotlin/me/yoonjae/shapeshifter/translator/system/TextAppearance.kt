package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.declaration.FunctionDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val textAppearance = SwiftFile("TextAppearance.swift") {
    import("UIKit")
    struct("TextAppearance") {
        textAppearance("body1", "textColorPrimary", 14)
        textAppearance("body2", "textColorPrimary", 14)
        textAppearance("button", "textColorPrimary", 14)
        textAppearance("caption", "textColorSecondary", 12)
        textAppearance("display1", "textColorSecondary", 34)
        textAppearance("display2", "textColorSecondary", 45)
        textAppearance("display3", "textColorSecondary", 56)
        textAppearance("display4", "textColorSecondary", 112)
        textAppearance("headline", "textColorPrimary", 24)
        textAppearance("large", "textColorPrimary", 22)
        textAppearance("medium", "textColorSecondary", 18)
        textAppearance("small", "textColorTertiary", 14)
        textAppearance("subhead", "textColorPrimary", 16)
        textAppearance("title", "textColorPrimary", 20)
        variable("textColor", Type("UIColor"))
        variable("textSize", Type("CGFloat"))
    }
}

private fun FunctionDescriber.textAppearance(name: String, textColor: String, textSize: Int) {
    function(name, Type("TextAppearance")) {
        static()
        parameter("theme", Type("Theme"))
        returnStatement {
            initializerExpression("TextAppearance") {
                argument("textColor", "theme.$textColor")
                argument("textSize", textSize.toString())
            }
        }
    }
}
