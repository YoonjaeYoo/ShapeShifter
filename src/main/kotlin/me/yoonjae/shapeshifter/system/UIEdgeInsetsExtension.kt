package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.type.Type

class UIEdgeInsetsExtension : me.yoonjae.shapeshifter.poet.file.SwiftFile("UIEdgeInsetsExtension.swift") {
    init {
        import("CoreGraphics")
        import("UIKit")

        extension("UIEdgeInsets") {
            initializer {
                parameter("inset", Type("CGFloat"), label = "_")

                initializerExpression("self") {
                    argument("top", "inset")
                    argument("left", "inset")
                    argument("bottom", "inset")
                    argument("right", "inset")
                }
            }
        }
    }
}
