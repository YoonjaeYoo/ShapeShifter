package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class Theme : SwiftFile("Theme.swift") {
    companion object {
        val FIELDS = mapOf(
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
                "windowBackground" to Type("UIColor", true)
        )
    }

    init {
        import("UIKit")
        clazz("Theme") {
            public()
            FIELDS.forEach { name, type -> constant(name, type) }

            initializer {
                FIELDS.forEach { name, type -> parameter(name, type, "nil") }

                FIELDS.forEach { name, _ -> assignmentExpression("self.$name", name) }
            }
        }
    }
}
