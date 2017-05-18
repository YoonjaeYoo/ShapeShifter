package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val frameLayout = SwiftFile("FrameLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("FrameLayout") {
        open()
        superType("ViewGroup")

        initializer {
            override()
            public()
            parameter("layoutParams", Type("ViewGroup.LayoutParams"))
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

            initializerExpression("super") {
                argument("layoutParams", "layoutParams")
                argument("id", "id")
                argument("padding", "padding")
                argument("minWidth", "minWidth")
                argument("minHeight", "minHeight")
                argument("alpha", "alpha")
                argument("background", "background")
                argument("sublayouts", "sublayouts")
                argument("config", "config")
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
                            argument("width",
                                    "layoutParams.width == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                            "(layoutParams.width == WRAP_CONTENT ? 0 : layoutParams.width)")
                            argument("height",
                                    "layoutParams.height == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                            "(layoutParams.height == WRAP_CONTENT ? 0 : layoutParams.height)")
                        }
                    }
                }
            }
            constant("measurements", Type("[LayoutMeasurement]")) {
                functionCallExpression("sublayouts.map") {
                    trailingClosure {
                        closureParameter("sublayout")
                        constant("measurement") {
                            functionCallExpression("sublayout.measurement") {
                                argument("within",
                                        "maxSize.decreasedByInsets(layoutParams.margin)" +
                                                ".decreasedByInsets(padding)")
                            }
                        }
                        ifStatement("layoutParams.width == WRAP_CONTENT") {
                            codeBlock {
                                assignmentExpression("size.width") {
                                    functionCallExpression("max") {
                                        argument(value = "size.width")
                                        argument(value = "measurement.size.width + padding.left +" +
                                                " padding.right + layoutParams.margin.left + " +
                                                "layoutParams.margin.right")
                                    }

                                }
                            }
                        }
                        ifStatement("layoutParams.height == WRAP_CONTENT") {
                            codeBlock {
                                assignmentExpression("size.height") {
                                    functionCallExpression("max") {
                                        argument(value = "size.height")
                                        argument(value = "measurement.size.height + padding.top +" +
                                                " padding.bottom + layoutParams.margin.top + " +
                                                "layoutParams.margin.bottom")
                                    }
                                }
                            }
                        }
                        returnStatement("measurement")
                    }
                }
            }
            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size", "size")
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
            constant("sublayoutRect") {
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
                            returnStatement("measurement.arrangement(within: sublayoutRect)")
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

        clazz("LayoutParams") {
            superType("ViewGroup.LayoutParams")

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

            function("alignment", Type("Alignment")) {
                override()
                returnStatement("gravity.alignment")
            }
        }
    }
}
