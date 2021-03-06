package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class CGFloatExtension : SwiftFile("CGFloatExtension.swift") {
    init {
        import("CoreGraphics")
        import("UIKit")

        extension("CGFloat") {
            variable("roundedUpToFractionalPoint", Type("CGFloat")) {

                codeBlock {
                    ifStatement("self == 0") {
                        returnStatement("0")
                    }
                    ifStatement("self < 0") {
                        returnStatement("-(-self).roundedDownToFractionalPoint")
                    }
                    constant("scale", value = "UIScreen.main.scale")
                    constant("pointPrecision", value = "1.0 / scale")
                    ifStatement("self <= pointPrecision") {
                        returnStatement("pointPrecision")
                    }
                    returnStatement("ceil(self * scale) / scale")
                }
            }

            variable("roundedDownToFractionalPoint", Type("CGFloat")) {

                codeBlock {
                    ifStatement("self == 0") {
                        returnStatement("0")
                    }
                    ifStatement("self < 0") {
                        returnStatement("-(-self).roundedUpToFractionalPoint")
                    }
                    constant("scale", value = "UIScreen.main.scale")
                    constant("pointPrecision", value = "1.0 / scale")
                    ifStatement("self < pointPrecision") {
                        returnStatement("pointPrecision")
                    }
                    returnStatement("floor(self * scale) / scale")
                }
            }
        }
    }
}
