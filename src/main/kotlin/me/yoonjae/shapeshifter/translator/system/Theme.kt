package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val theme = SwiftFile("Theme.swift") {
    import("UIKit")
    struct("Theme") {
        constant("colorPrimary", Type("UIColor"))
        constant("colorPrimaryDark", Type("UIColor"))
        constant("colorAccent", Type("UIColor"))
        constant("textColorPrimary", Type("UIColor"))
        constant("textColorSecondary", Type("UIColor"))
        constant("textColorTertiary", Type("UIColor"))
        constant("textColorHint", Type("UIColor"))
        function("light", Type("Theme")) {
            static()
            returnStatement {
                initializerExpression("Theme") {
                    argument("colorPrimary", "Color.primary")
                    argument("colorPrimaryDark", "Color.primaryDark")
                    argument("colorAccent", "Color.accent")
                    colorArgument("textColorPrimary", "#000")
                    colorArgument("textColorSecondary", "#999")
                    colorArgument("textColorTertiary", "#999")
                    colorArgument("textColorHint", "#d3d4d4")
                }
            }
        }

        function("dark", Type("Theme")) {
            static()
            returnStatement {
                initializerExpression("Theme") {
                    argument("colorPrimary", "Color.primary")
                    argument("colorPrimaryDark", "Color.primaryDark")
                    argument("colorAccent", "Color.accent")
                    colorArgument("textColorPrimary", "#fff")
                    colorArgument("textColorSecondary", "#b3ffffff")
                    colorArgument("textColorTertiary", "#b3ffffff")
                    colorArgument("textColorHint", "#b3ffffff")
                }
            }
        }
    }
}

private fun ArgumentDescriber.colorArgument(name: String, value: String) {
    argument(name) {
        generalExpression("UIColor(\"$value\")")
    }
}
