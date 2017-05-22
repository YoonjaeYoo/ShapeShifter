package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.declaration.ClassDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val textAppearance = SwiftFile("TextAppearance.swift") {
    import("UIKit")
    clazz("TextAppearance") {
        public()
        initializer {
            public()
            parameter("theme", Type("Theme"), label = "_")
            parameter("textColor", Type("UIColor", true))
            parameter("textSize", Type("CGFloat", true))
            parameter("textStyle", Type("TextStyle", true))

            assignmentExpression("self.theme", "theme")
            assignmentExpression("self.textColor", "textColor")
            assignmentExpression("self.textSize", "textSize")
            assignmentExpression("self.textStyle", "textStyle")
        }
        variable("theme", Type("Theme"))
        variable("textColor", Type("UIColor", true))
        variable("textSize", Type("CGFloat", true))
        variable("textStyle", Type("TextStyle", true))

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
    }
}

private fun ClassDescriber.textAppearance(name: String, textColor: String, textSize: Int) {
    clazz(name.capitalize()) {
        public()
        superType("TextAppearance")
        initializer {
            public()
            parameter("theme", Type("Theme"), label = "_")
            initializerExpression("super") {
                argument(null, "theme")
                argument("textColor", "theme.$textColor")
                argument("textSize", textSize.toString())
                argument("textStyle", "nil")
            }
        }
    }
}
