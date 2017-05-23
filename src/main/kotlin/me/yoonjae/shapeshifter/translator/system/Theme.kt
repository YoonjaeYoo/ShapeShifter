package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val themeFields = mapOf(
        "colorBackground" to Type("UIColor", true),
        "colorPrimary" to Type("UIColor", true),
        "colorPrimaryDark" to Type("UIColor", true),
        "colorSecondary" to Type("UIColor", true),
        "colorAccent" to Type("UIColor", true),
        "colorControlActivated" to Type("UIColor", true),
        "colorControlNormal" to Type("UIColor", true),
        "colorControlHighlight" to Type("UIColor", true),
        "colorButtonNormal" to Type("UIColor", true),
        "textColorPrimary" to Type("UIColor", true),
        "textColorSecondary" to Type("UIColor", true),
        "textColorTertiary" to Type("UIColor", true),
        "textColorHint" to Type("UIColor", true),
        "windowBackground" to Type("Drawable", true)
)

val theme = SwiftFile("Theme.swift") {
    import("UIKit")
    clazz("Theme") {
        public()
        themeFields.forEach { name, type -> constant(name, type) }

        initializer {
            themeFields.forEach { name, type -> parameter(name, type, "nil") }

            themeFields.forEach { name, _ -> assignmentExpression("self.$name", name) }
        }
    }
}
