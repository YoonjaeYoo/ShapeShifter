package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class CGSizeExtension : SwiftFile("CGSizeExtension.swift") {
    init {
        import("CoreGraphics")
        import("UIKit")

        extension("CGSize") {
            function("decreasedByInsets", Type("CGSize")) {
                parameter("insets", Type("UIEdgeInsets"), label = "_")

                returnStatement {
                    initializerExpression("CGSize") {
                        argument("width", "width - insets.left - insets.right")
                        argument("height", "height - insets.top - insets.bottom")
                    }
                }
            }

            function("increasedByInsets", Type("CGSize")) {
                parameter("insets", Type("UIEdgeInsets"), label = "_")

                returnStatement {
                    initializerExpression("CGSize") {
                        argument("width", "width + insets.left + insets.right")
                        argument("height", "height + insets.top + insets.bottom")
                    }
                }
            }

            function("decreasedToSize", Type("CGSize")) {
                parameter("maxSize", Type("CGSize"), label = "_")

                constant("width", value = "min(self.width, maxSize.width)")
                constant("height", value = "min(self.height, maxSize.height)")
                returnStatement {
                    initializerExpression("CGSize") {
                        argument("width", "width")
                        argument("height", "height")
                    }
                }
            }

            function("increasedToSize", Type("CGSize")) {
                parameter("minSize", Type("CGSize"), label = "_")

                constant("width", value = "max(self.width, minSize.width)")
                constant("height", value = "max(self.height, minSize.height)")
                returnStatement {
                    initializerExpression("CGSize") {
                        argument("width", "width")
                        argument("height", "height")
                    }
                }
            }
        }
    }
}
