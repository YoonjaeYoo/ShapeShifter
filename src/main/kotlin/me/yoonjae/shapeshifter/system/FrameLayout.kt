package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize

class FrameLayout : SwiftFile("FrameLayout.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("FrameLayout") {
            public()
            superType("ViewGroup")

            initializer {
                override()
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
                parameter("config", Type("(FrameLayout) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                    argument("children", "children")
                }
                assignmentExpression("self.config") {
                    closureExpression {
                        closureParameter("view")

                        functionCallExpression("config?") {
                            argument(value = "self")
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
                variable("minSize", value = "CGSize()")
                constant("measurements", Type("[LayoutMeasurement]")) {
                    functionCallExpression("children.map") {
                        trailingClosure {
                            closureParameter("child")
                            constant("measurement") {
                                functionCallExpression("child.measurement") {
                                    argument("within", "size.decreasedByInsets(layoutParams.margin)" +
                                            ".decreasedByInsets(padding)")
                                }
                            }
                            assignmentExpression("minSize.width", "max(minSize.width, " +
                                    "measurement.size.width + padding.left + padding.right + " +
                                    "layoutParams.margin.left + layoutParams.margin.right)")
                            assignmentExpression("minSize.height", "max(minSize.height, " +
                                    "measurement.size.height + padding.top + padding.bottom + " +
                                    "layoutParams.margin.top + layoutParams.margin.bottom)")
                            returnStatement("measurement")
                        }
                    }
                }
                ifStatement("layoutParams.width == WRAP_CONTENT") {
                    assignmentExpression("size.width", "minSize.width")
                }
                ifStatement("layoutParams.height == WRAP_CONTENT") {
                    assignmentExpression("size.height", "minSize.height")
                }
                returnStatement {
                    functionCallExpression("LayoutMeasurement") {
                        argument("layout", "self")
                        argument("size") {
                            increasedToMinSize()
                        }
                        argument("maxSize", "maxSize")
                        argument("sublayouts", "measurements")
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

        clazz("FrameLayoutParams") {
            superType("LayoutParams")

            public()
            constant("gravity", Type("Gravity"))

            initializer {
                parameter("width", Type("CGFloat"))
                parameter("height", Type("CGFloat"))
                parameter("margin", Type("UIEdgeInsets"), "UIEdgeInsets()")
                parameter("gravity", Type("Gravity"), "[]")

                assignmentExpression("self.gravity", "gravity")
                initializerExpression("super") {
                    argument("width", "width")
                    argument("height", "height")
                    argument("margin", "margin")
                }
            }

            function("arrangement", Type("CGRect")) {
                override()
                parameter("size", Type("CGSize"))
                parameter("rect", Type("CGRect"), label = "in")

                returnStatement {
                    functionCallExpression("gravity.alignment.position") {
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
