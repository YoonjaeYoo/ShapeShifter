package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val view = SwiftFile("View.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("View") {
        open()
        genericParameter("V") {
            superType("UIView")
        }
        superType("ConfigurableLayout")

        constant("theme", Type("Theme")) { open() }
        constant("layoutParams", Type("LayoutParams")) { open() }
        constant("viewReuseId", Type("String", true)) { open() }
        constant("padding", Type("UIEdgeInsets")) { open() }
        constant("minWidth", Type("CGFloat", true)) { open() }
        constant("minHeight", Type("CGFloat", true)) { open() }
        constant("config", Type("(V) -> Void", true)) { open() }
        constant("flexibility", value = "Flexibility.inflexible") { open() }
        variable("needsView", Type("Bool")) {
            open()
            codeBlock {
                returnStatement("config != nil")
            }
        }

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
            parameter("config", Type("(V) -> Void", true), "nil")

            assignmentExpression("self.theme", "theme")
            assignmentExpression("self.layoutParams", "layoutParams")
            assignmentExpression("self.viewReuseId", "id")
            assignmentExpression("self.padding", "padding")
            assignmentExpression("self.minWidth", "minWidth")
            assignmentExpression("self.minHeight", "minHeight")
            assignmentExpression("self.config") {
                closureExpression {
                    closureParameter("view")
                    assignmentExpression("view.alpha", "alpha")
                    assignmentExpression("view.backgroundColor", "background")
                    functionCallExpression("config?") {
                        argument(value = "view")
                    }
                }
            }
        }

        function("configure") {
            open()
            parameter("view", Type("V"))

            generalExpression("config?(view)")
        }

        function("makeView", Type("UIView")) {
            open()
            returnStatement("V()")
        }

        function("measurement", Type("LayoutMeasurement")) {
            public()
            parameter("maxSize", Type("CGSize"), label = "within")

            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size", "maxSize")
                    argument("maxSize", "maxSize")
                    argument("sublayouts", "[]")
                }
            }
        }

        function("arrangement", Type("LayoutArrangement")) {
            public()
            parameter("rect", Type("CGRect"), label = "within")
            parameter("measurement", Type("LayoutMeasurement"))

            returnStatement {
                functionCallExpression("LayoutArrangement") {
                    argument("layout", "self")
                    argument("frame", "rect")
                    argument("sublayouts", "[]")
                }
            }
        }
    }
}
