package me.yoonjae.shapeshifter.translator.requirements

import me.yoonjae.shapeshifter.poet.declaration.ConstantDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile

val appTheme = SwiftFile("AppTheme.swift") {
    import("UIKit")
    struct("AppTheme") {
        struct("Light") {
            constant("colorPrimary", "Color.primary") { static() }
            constant("colorPrimaryDark", "Color.primaryDark") { static() }
            constant("colorAccent", "Color.accent") { static() }
            color("textColorPrimary", "#000")
            color("textColorSecondary", "#999999")
            color("textColorHint", "#d3d4d4")
        }

        struct("Dark") {
            constant("colorPrimary", "Color.primary") { static() }
            constant("colorPrimaryDark", "Color.primaryDark") { static() }
            constant("colorAccent", "Color.accent") { static() }
            color("textColorPrimary", "#000")
            color("textColorSecondary", "#999999")
            color("textColorHint", "#d3d4d4")
        }
    }
}

private fun ConstantDescriber.color(name: String, value: String) {
    constant(name) {
        static()
        generalExpression("UIColor(\"$value\")")
    }
}
