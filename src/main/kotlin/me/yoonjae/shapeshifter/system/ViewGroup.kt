package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class ViewGroup : SwiftFile("ViewGroup.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        constant("MATCH_PARENT", Type("CGFloat"), "-1")
        constant("WRAP_CONTENT", Type("CGFloat"), "-2")

        clazz("ViewGroup") {
            public()
            superType("View") {
                genericParameter("UIView")
            }

            variable("children", value = "[Layout]()") { public() }

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
                parameter("children", Type("[Layout]"), "[]")
                parameter("config", Type("(UIView) -> Void", true), "nil")

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
                forInStatement("child", "children") {
                    ifStatement("child is BaseView") {
                        variable("view", value = "child as! BaseView")
                        assignmentExpression("view.parent", "self")
                    }
                    functionCallExpression("self.children.append") {
                        argument(value = "child")
                    }
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

            function("findView", Type("BaseView", true)) {
                override()
                public()
                parameter("id", Type("String"), label = "by")

                forInStatement("child", "children") {
                    ifStatement("child is BaseView") {
                        constant("base", value = "child as! BaseView")
                        constant("view", value = "base.findView(by: id)")
                        ifStatement("view != nil") {
                            returnStatement("view")
                        }
                    }
                }
                returnStatement("id == self.viewReuseId ? self : nil")
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
                    functionCallExpression("Alignment.topLeading.position") {
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
}
