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
            superType("BaseView")
            superType("ConfigurableLayout")

            variable("theme", Type("Theme")) { public() }
            variable("id", Type("String", true)) { public() }
            variable("layoutParams", Type("LayoutParams")) { public() }
            variable("padding", Type("UIEdgeInsets")) { public() }
            variable("minWidth", Type("CGFloat", true)) { public() }
            variable("minHeight", Type("CGFloat", true)) { public() }
            variable("alpha", Type("CGFloat"), "1.0") { public() }
            variable("background", Type("UIColor", true), "nil") { public() }
            variable("config", Type("(View) -> Void", true)) { public() }
            variable("flexibility", value = "Flexibility.inflexible") { public() }
            variable("needsView", Type("Bool"), "true") { public() }
            variable("viewReuseId", Type("String", true)) {
                public()

                codeBlock {
                    returnStatement("id")
                }
            }
            variable("parent", Type("BaseView", true), "nil") { public() }
            variable("view", Type("V", true)) {
                public()

                codeBlock {
                    returnStatement("_view")
                }
            }
            variable("_view", Type("V", true)) { private() }

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
                parameter("config", Type("(View) -> Void", true), "nil")

                assignmentExpression("self.theme", "theme")
                assignmentExpression("self.layoutParams", "layoutParams")
                assignmentExpression("self.id", "id")
                assignmentExpression("self.padding", "padding")
                assignmentExpression("self.minWidth", "minWidth")
                assignmentExpression("self.minHeight", "minHeight")
                assignmentExpression("self.alpha", "alpha")
                assignmentExpression("self.background", "background")
                assignmentExpression("self.config") {
                    closureExpression {
                        closureParameter("view")
                        assignmentExpression("view.view!.alpha", "self.alpha")
                        assignmentExpression("view.view!.backgroundColor", "self.background")
                    }
                }
            }

            function("configure") {
                public()
                parameter("view", Type("V"))

                assignmentExpression("_view", "view")
                generalExpression("config?(self)")
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

            function("findView", Type("BaseView", true)) {
                public()
                parameter("id", Type("String"), label = "by")

                returnStatement("id == self.viewReuseId ? self : nil")
            }

            function("requestLayout") {
                public()
                parameter("view", Type("UIView"), label = "in")

                ifStatement("parent == nil") {
                    functionCallExpression("DispatchQueue.global(qos: .background).async") {
                        trailingClosure {
                            constant("arrangement",
                                    value = "self.arrangement(width: view.bounds.width, " +
                                            "height: view.bounds.height)")
                            functionCallExpression("DispatchQueue.main.async") {
                                argument("execute") {
                                    closureExpression {
                                        generalExpression("arrangement.makeViews(in: view)")
                                    }
                                }
                            }
                        }
                    }
                    elseClause {
                        generalExpression("parent!.requestLayout(in: view)")
                    }
                }
            }
        }
    }
}
