package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize

class View : SwiftFile("View.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("View") {
            public()
            genericParameter("V") {
                superType("UIView")
            }
            superType("ConfigurableLayout")

            constant("theme", Type("Theme")) { public() }
            constant("layoutParams", Type("LayoutParams")) { public() }
            constant("id", Type("String", true)) { public() }
            constant("padding", Type("UIEdgeInsets")) { public() }
            constant("minWidth", Type("CGFloat", true)) { public() }
            constant("minHeight", Type("CGFloat", true)) { public() }
            constant("config", Type("(V) -> Void", true)) { public() }
            constant("flexibility", value = "Flexibility.inflexible") { public() }
            variable("needsView", Type("Bool"), "true") { public() }
            variable("viewReuseId", Type("String", true)) {
                public()

                codeBlock {
                    returnStatement("id")
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
                assignmentExpression("self.id", "id")
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
                public()
                parameter("view", Type("V"))

                generalExpression("config?(view)")
            }

            function("makeView", Type("UIView")) {
                public()
                returnStatement("V()")
            }

            function("measurement", Type("LayoutMeasurement")) {
                public()
                parameter("maxSize", Type("CGSize"), label = "within")

                constant("size") {
                    functionCallExpression("maxSize.decreasedToSize") {
                        argument {
                            functionCallExpression("CGSize") {
                                argument("width",
                                        "layoutParams.width == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.width == WRAP_CONTENT ? " +
                                                "padding.left + padding.right : " +
                                                "layoutParams.margin.left + layoutParams.margin.right + " +
                                                "layoutParams.width)")
                                argument("height",
                                        "layoutParams.height == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.height == WRAP_CONTENT ? " +
                                                "padding.top + padding.bottom : " +
                                                "layoutParams.margin.top + layoutParams.margin.bottom + " +
                                                "layoutParams.height)")
                            }
                        }
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutMeasurement") {
                        argument("layout", "self")
                        argument("size") {
                            increasedToMinSize()
                        }
                        argument("maxSize", "maxSize")
                        argument("sublayouts", "[]")
                    }
                }
            }

            function("arrangement", Type("LayoutArrangement")) {
                public()
                parameter("rect", Type("CGRect"), label = "within")
                parameter("measurement", Type("LayoutMeasurement"))

                constant("frame") {
                    functionCallExpression("layoutParams.arrangement") {
                        argument("size", "measurement.size")
                        argument("in", "rect")
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutArrangement") {
                        argument("layout", "self")
                        argument("frame", "frame")
                        argument("sublayouts", "[]")
                    }
                }
            }
        }
    }
}
