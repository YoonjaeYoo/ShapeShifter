package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val viewGroup = SwiftFile("ViewGroup.swift") {
    import("UIKit")
    import("LayoutKit")

    constant("MATCH_PARENT", Type("CGFloat"), "-1")
    constant("WRAP_CONTENT", Type("CGFloat"), "-2")

    clazz("ViewGroup") {
        open()
        superType("View") {
            genericParameter("UIView")
        }

        constant("sublayouts", Type("[Layout]")) { open() }
        initializer {
            public()
            parameter("layoutParams", Type("LayoutParams"))
            parameter("id", Type("String?"), "nil")
            parameter("padding", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("minWidth", Type("CGFloat?"), "nil")
            parameter("minHeight", Type("CGFloat?"), "nil")
            parameter("alpha", Type("CGFloat"), "1.0")
            parameter("background", Type("UIColor?"), "nil")
            parameter("sublayouts", Type("[Layout]"), "[]")
            parameter("config", Type("((UIView) -> Void)?"), "nil")

            assignmentExpression("self.sublayouts", "sublayouts")
            initializerExpression("super") {
                argument("layoutParams", "layoutParams")
                argument("id", "id")
                argument("padding", "padding")
                argument("minWidth", "minWidth")
                argument("minHeight", "minHeight")
                argument("alpha", "alpha")
                argument("background", "background")
                argument("config", "config")
            }
        }

        function("measurement", Type("LayoutMeasurement")) {
            override()
            public()
            parameter("maxSize", Type("CGSize"), label = "within")

            functionCallExpression("fatalError") {
                argument(value = "\"not implemented\"")
            }
        }

        function("arrangement", Type("LayoutArrangement")) {
            override()
            public()
            parameter("rect", Type("CGRect"), label = "within")
            parameter("measurement", Type("LayoutMeasurement"))

            functionCallExpression("fatalError") {
                argument(value = "\"not implemented\"")
            }
        }

        clazz("LayoutParams") {
            public()
            constant("width", Type("CGFloat"))
            constant("height", Type("CGFloat"))
            constant("margin", Type("UIEdgeInsets"))

            initializer {
                parameter("width", Type("CGFloat"))
                parameter("height", Type("CGFloat"))
                parameter("margin", Type("UIEdgeInsets")) {
                    initializerExpression("UIEdgeInsets")
                }

                assignmentExpression("self.width", "width")
                assignmentExpression("self.height", "height")
                assignmentExpression("self.margin", "margin")
            }

            function("arrangement", Type("CGRect")) {
                parameter("size", Type("CGSize"))
                parameter("rect", Type("CGRect"), label = "in")

                returnStatement {
                    functionCallExpression("alignment().position") {
                        argument("size", "size.decreasedByInsets(margin)")
                        argument("in") {
                            initializerExpression("CGRect") {
                                argument("origin") {
                                    initializerExpression("CGPoint") {
                                        argument("x", "rect.origin.x + margin.left")
                                        argument("y", "rect.origin.y + margin.top")
                                    }
                                }
                                argument("size", "rect.size.decreasedByInsets(margin)")
                            }
                        }
                    }
                }
            }

            function("alignment", Type("Alignment")) {
                returnStatement(".fill")
            }
        }
    }
}
