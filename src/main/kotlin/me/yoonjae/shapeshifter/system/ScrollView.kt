package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize

class ScrollView : SwiftFile("ScrollView.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("ScrollView") {
            superType("View") {
                genericParameter("UISmartScrollView")
            }

            variable("child", Type("Layout", true)) {
                public()
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
                parameter("child", Type("Layout", true), "nil")
                parameter("config", Type("(ScrollView) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                }
                assignmentExpression("self.config") {
                    closureExpression {
                        closureParameter("view")

                        functionCallExpression("config?") {
                            argument(value = "self")
                        }
                    }
                }
                ifStatement("child is BaseView") {
                    variable("view", value = "child as! BaseView")
                    assignmentExpression("view.parent", "self")
                }
                assignmentExpression("self.child", "child")
            }

            function("measurement", Type("LayoutMeasurement")) {
                override()
                public()
                parameter("maxSize", Type("CGSize"), label = "within")

                variable("size") {
                    functionCallExpression("maxSize.decreasedToSize") {
                        argument {
                            functionCallExpression("CGSize") {
                                argument("width", "layoutParams.width == MATCH_PARENT || " +
                                        "layoutParams.width == WRAP_CONTENT ? " +
                                        ".greatestFiniteMagnitude : " +
                                        "layoutParams.width + layoutParams.margin.left + " +
                                        "layoutParams.margin.right")
                                argument("height", "layoutParams.height == MATCH_PARENT || " +
                                        "layoutParams.height == WRAP_CONTENT ? " +
                                        ".greatestFiniteMagnitude : " +
                                        "layoutParams.height + layoutParams.margin.top + " +
                                        "layoutParams.margin.bottom")
                            }
                        }
                    }
                }
                constant("sublayoutSize") {
                    initializerExpression("CGSize") {
                        argument("width", "size.width")
                        argument("height", ".greatestFiniteMagnitude")
                    }
                }
                constant("measurement") {
                    functionCallExpression("child?.measurement") {
                        argument("within", "sublayoutSize.decreasedByInsets" +
                                "(layoutParams.margin).decreasedByInsets(padding)")
                    }
                }
                ifStatement("let measurement = measurement") {
                    ifStatement("layoutParams.width == WRAP_CONTENT") {
                        assignmentExpression("size.width", "max(size.width, " +
                                "measurement.size.width + padding.left + " +
                                "padding.right + layoutParams.margin.left + " +
                                "layoutParams.margin.right)")
                    }
                    ifStatement("layoutParams.height == WRAP_CONTENT") {
                        assignmentExpression("size.height", "max(size.height, " +
                                "measurement.size.height + padding.top + " +
                                "padding.bottom + layoutParams.margin.top + " +
                                "layoutParams.margin.bottom)")
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutMeasurement") {
                        argument("layout", "self")
                        argument("size") {
                            increasedToMinSize()
                        }
                        argument("maxSize", "maxSize")
                        argument("sublayouts", "measurement == nil ? [] : [measurement!]")
                    }
                }
            }

            function("arrangement", Type("LayoutArrangement")) {
                override()
                public()
                parameter("rect", Type("CGRect"), label = "within")
                parameter("measurement", Type("LayoutMeasurement"))

                constant("frame") {
                    functionCallExpression("layoutParams.arrangement") {
                        argument("size", "measurement.size")
                        argument("in", "rect")
                    }
                }
                constant("arrangements", Type("[LayoutArrangement]")) {
                    functionCallExpression("measurement.sublayouts.map") {
                        trailingClosure {
                            closureParameter("measurement")
                            constant("childRect") {
                                initializerExpression("CGRect") {
                                    argument("origin") {
                                        initializerExpression("CGPoint") {
                                            argument("x", "padding.left")
                                            argument("y", "padding.top")
                                        }
                                    }
                                    argument("size") {
                                        initializerExpression("CGSize") {
                                            argument("width", "frame.width - padding.left - " +
                                                    "padding.right")
                                            argument("height", "measurement.size.height")
                                        }
                                    }
                                }
                            }
                            returnStatement("measurement.arrangement(within: childRect)")
                        }
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutArrangement") {
                        argument("layout", "self")
                        argument("frame", "frame")
                        argument("sublayouts", "arrangements")
                    }
                }
            }

            function("findView", Type("BaseView", true)) {
                override()
                public()
                parameter("id", Type("String"), label = "by")

                ifStatement("let child = child") {
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
    }
}