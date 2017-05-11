package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val frameLayout = SwiftFile("FrameLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("FrameLayout") {
        open()
        genericParameter("V") {
            superType("View")
        }
        superType("StandardLayout") {
            genericParameter("UIView")
        }

        initializer {
            public()
            parameter("width", Type("CGFloat?"), "nil")
            parameter("height", Type("CGFloat?"), "nil")
            parameter("margin", Type("EdgeInsets")) {
                initializerExpression("EdgeInsets")
            }
            parameter("alignment", Type("Alignment"), ".fill")
            parameter("flexibility", Type("Flexibility"), ".flexible")
            parameter("viewReuseId", Type("String?"), "nil")
            parameter("sublayouts", Type("[Layout]"))
            parameter("config", Type("((V) -> Void)?"), "nil")

            initializerExpression("super") {
                argument("width", "width")
                argument("height", "height")
                argument("margin", "margin")
                argument("alignment", "alignment")
                argument("flexibility", "flexibility")
                argument("viewReuseId", "viewReuseId")
                argument("sublayout") {
                    functionCallExpression("PileLayout<V>.init") {
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
