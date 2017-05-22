package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val linearLayout = SwiftFile("LinearLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("LinearLayout") {
        open()
        genericParameter("V") {
            superType("View")
        }
        superType("AndroidLayout") {
            genericParameter("UIView")
        }

        initializer {
            public()
            parameter("axis", Type("Axis"))
            parameter("width", Type("CGFloat"))
            parameter("height", Type("CGFloat"))
            parameter("margin", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("padding", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("alignment", Type("Alignment"), ".fill")
            parameter("flexibility", Type("Flexibility"), ".flexible")
            parameter("viewReuseId", Type("String", true), "nil")
            parameter("sublayouts", Type("[Layout]"))
            parameter("config", Type("(V) -> Void", true), "nil")

            initializerExpression("super") {
                argument("width", "width")
                argument("height", "height")
                argument("margin", "margin")
                argument("padding", "padding")
                argument("alignment", "alignment")
                argument("flexibility", "flexibility")
                argument("viewReuseId", "viewReuseId")
                argument("sublayout") {
                    functionCallExpression("StackLayout<V>.init") {
                        argument("axis", "axis")
                        argument("sublayouts", "sublayouts")
                        trailingClosure {
                            closureParameter("view")
                            functionCallExpression("config?") {
                                argument(null, "view")
                            }
                        }
                    }
                }
            }
        }
    }
}
