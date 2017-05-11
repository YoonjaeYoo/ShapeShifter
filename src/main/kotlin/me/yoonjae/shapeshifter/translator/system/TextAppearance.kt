package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.declaration.FunctionDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val textAppearance = SwiftFile("TextAppearance.swift") {
    import("UIKit")
    struct("TextAppearance") {
        textAppearance("body1", "textColorPrimary")
        textAppearance("body2", "textColorPrimary")
        textAppearance("button", "textColorPrimary")
        textAppearance("caption", "textColorSecondary")
        textAppearance("display1", "textColorSecondary")
        textAppearance("display2", "textColorSecondary")
        textAppearance("display3", "textColorSecondary")
        textAppearance("display4", "textColorSecondary")
        textAppearance("headline", "textColorPrimary")
        textAppearance("large", "textColorPrimary")
        textAppearance("medium", "textColorSecondary")
        textAppearance("small", "textColorTertiary")
        textAppearance("subhead", "textColorPrimary")
        textAppearance("title", "textColorPrimary")
        variable("font", Type("UIFont"))
        variable("textColor", Type("UIColor"))
    }
}

private fun FunctionDescriber.textAppearance(name: String, textColor: String) {
    function(name, Type("TextAppearance")) {
        static()
        parameter("theme", Type("Theme"))
        returnStatement {
            initializerExpression("TextAppearance") {
                argument("font", "Font.$name")
                argument("textColor", "theme.$textColor")
            }
        }
    }
}
