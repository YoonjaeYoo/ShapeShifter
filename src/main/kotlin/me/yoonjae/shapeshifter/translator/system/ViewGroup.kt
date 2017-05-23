package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val viewGroup = SwiftFile("ViewGroup.swift") {
    import("UIKit")
    import("LayoutKit")

    constant("MATCH_PARENT", Type("CGFloat"), "-1")
    constant("WRAP_CONTENT", Type("CGFloat"), "-2")

    clazz("ViewGroup") {
        public()
        superType("View") {
            genericParameter("UIView")
        }

        constant("sublayouts", Type("[Layout]")) { public() }
        initializer {
            public()
            parameter("theme", Type("Theme"), "AppTheme()")
            parameter("id", Type("String", true), "nil")
            parameter("layoutParams", Type("LayoutParams"))
            parameter("padding", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("minWidth", Type("CGFloat", true), "nil")
            parameter("minHeight", Type("CGFloat", true), "nil")
            parameter("alpha", Type("CGFloat"), "1.0")
            parameter("background", Type("UIColor", true), "nil")
            parameter("sublayouts", Type("[Layout]"), "[]")
            parameter("config", Type("(UIView) -> Void", true), "nil")

            assignmentExpression("self.sublayouts", "sublayouts")
            initializerExpression("super") {
                argument("theme", "theme")
                argument("id", "id")
                argument("layoutParams", "layoutParams")
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
                functionCallExpression("Alignment.fill.position") {
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
    }
}
