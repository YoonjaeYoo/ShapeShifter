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
                genericParameter("UIScrollView")
            }

            constant("child", Type("Layout", true)) {
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
                parameter("config", Type("(UIScrollView) -> Void", true), "nil")

                assignmentExpression("self.child", "child")
                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                    trailingClosure {
                        closureParameter("scrollView")

                        ifStatement("!scrollView.subviews.isEmpty") {
                            codeBlock {
                                assignmentExpression("scrollView.contentSize") {
                                    initializerExpression("CGSize") {
                                        argument("width", "scrollView.frame.width")
                                        argument("height", "scrollView.subviews[0].frame.height")
                                    }
                                }
                            }
                        }
                        functionCallExpression("config?") {
                            argument(value = "scrollView")
                        }
                    }
                }
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
                    codeBlock {
                        ifStatement("layoutParams.width == WRAP_CONTENT") {
                            codeBlock {
                                assignmentExpression("size.width", "max(size.width, " +
                                        "measurement.size.width + padding.left + " +
                                        "padding.right + layoutParams.margin.left + " +
                                        "layoutParams.margin.right)")
                            }
                        }
                        ifStatement("layoutParams.height == WRAP_CONTENT") {
                            codeBlock {
                                assignmentExpression("size.height", "max(size.height, " +
                                        "measurement.size.height + padding.top + " +
                                        "padding.bottom + layoutParams.margin.top + " +
                                        "layoutParams.margin.bottom)")
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
                constant("childRect") {
                    initializerExpression("CGRect") {
                        argument("origin") {
                            initializerExpression("CGPoint") {
                                argument("x", "padding.left")
                                argument("y", "padding.top")
                            }
                        }
                        argument("size", "frame.size.decreasedByInsets(padding)")
                    }
                }
                constant("arrangements", Type("[LayoutArrangement]")) {
                    functionCallExpression("measurement.sublayouts.map") {
                        trailingClosure {
                            closureParameter("measurement") {
                                returnStatement("measurement.arrangement(within: childRect)")
                            }
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

        }
    }
}