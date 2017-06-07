package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.declaration.ClassDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class TextAppearance : SwiftFile("TextAppearance.swift") {
    init {
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

            clazz("AppCompat") {
                public()
                superType("TextAppearance")

                textAppearance("Body1", "textColorPrimary", 14)
                textAppearance("Body2", "textColorPrimary", 14)
                textAppearance("Button", "textColorPrimary", 14)
                textAppearance("Caption", "textColorSecondary", 12)
                textAppearance("Display1", "textColorSecondary", 34)
                textAppearance("Display2", "textColorSecondary", 45)
                textAppearance("Display3", "textColorSecondary", 56)
                textAppearance("Display4", "textColorSecondary", 112)
                textAppearance("Headline", "textColorPrimary", 24)
                textAppearance("Large", "textColorPrimary", 22)
                textAppearance("Medium", "textColorSecondary", 18)
                textAppearance("Small", "textColorTertiary", 14)
                textAppearance("Subhead", "textColorPrimary", 16)
                textAppearance("Title", "textColorPrimary", 20)
            }
        }
    }

    private fun ClassDescriber.textAppearance(name: String, textColor: String, textSize: Int) {
        clazz(name) {
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
}
