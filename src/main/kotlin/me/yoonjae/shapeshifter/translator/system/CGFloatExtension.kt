package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val cgFloatExtension = SwiftFile("CGFloatExtension.swift") {
    import("CoreGraphics")
    import("UIKit")

    extension("CGFloat") {
        variable("roundedUpToFractionalPoint", Type("CGFloat")) {

            codeBlock {
                ifStatement("self == 0") {
                    codeBlock {
                        returnStatement("0")
                    }
                }
                ifStatement("self < 0") {
                    codeBlock {
                        returnStatement("-(-self).roundedDownToFractionalPoint")
                    }
                }
                constant("scale", value = "UIScreen.main.scale")
                constant("pointPrecision", value = "1.0 / scale")
                ifStatement("self <= pointPrecision") {
                    codeBlock {
                        returnStatement("pointPrecision")
                    }
                }
                returnStatement("ceil(self * scale) / scale")
            }
        }

        variable("roundedDownToFractionalPoint", Type("CGFloat")) {

            codeBlock {
                ifStatement("self == 0") {
                    codeBlock {
                        returnStatement("0")
                    }
                }
                ifStatement("self < 0") {
                    codeBlock {
                        returnStatement("-(-self).roundedUpToFractionalPoint")
                    }
                }
                constant("scale", value = "UIScreen.main.scale")
                constant("pointPrecision", value = "1.0 / scale")
                ifStatement("self < pointPrecision") {
                    codeBlock {
                        returnStatement("pointPrecision")
                    }
                }
                returnStatement("floor(self * scale) / scale")
            }
        }
    }
}
