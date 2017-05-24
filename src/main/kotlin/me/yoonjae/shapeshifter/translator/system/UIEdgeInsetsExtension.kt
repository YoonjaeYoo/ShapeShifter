package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val uiEdgeInsetsExtension = SwiftFile("UIEdgeInsetsExtension.swift") {
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
