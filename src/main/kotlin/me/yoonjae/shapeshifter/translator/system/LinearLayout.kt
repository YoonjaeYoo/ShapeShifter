package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val linearLayout = SwiftFile("LinearLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("LinearLayout") {
        public()
        superType("ViewGroup")

        enum("Orientation") {
            public()
            case("vertical")
            case("horizontal")
        }

        constant("orientation", Type("Orientation")) { public() }
        initializer {
            public()
            parameter("theme", Type("Theme"), "AppTheme()")
            parameter("id", Type("String", true), "nil")
            parameter("layoutParams", Type("LayoutParams"))
            parameter("orientation", Type("Orientation"))
            parameter("padding", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("minWidth", Type("CGFloat", true), "nil")
            parameter("minHeight", Type("CGFloat", true), "nil")
            parameter("alpha", Type("CGFloat"), "1.0")
            parameter("background", Type("UIColor", true), "nil")
            parameter("sublayouts", Type("[Layout]"), "[]")
            parameter("config", Type("(UIView) -> Void", true), "nil")

            assignmentExpression("self.orientation", "orientation")
            initializerExpression("super") {
                argument("theme", "theme")
                argument("id", "id")
                argument("layoutParams", "layoutParams")
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
            variable("sublayoutSize") {
                generalExpression("maxSize.decreasedByInsets(layoutParams.margin)" +
                        ".decreasedByInsets(padding)")
            }
            constant("measurements", Type("[LayoutMeasurement]")) {
                functionCallExpression("sublayouts.map") {
                    trailingClosure {
                        closureParameter("sublayout")
                        constant("measurement") {
                            functionCallExpression("sublayout.measurement") {
                                argument("within", "sublayoutSize")
                            }
                        }
                        ifStatement("orientation == .vertical") {
                            codeBlock {
                                generalExpression("sublayoutSize.height -= " +
                                        "measurement.size.height")
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
                                        generalExpression("size.height += measurement.size.height")
                                    }
                                }
                            }
                            elseClause {
                                codeBlock {
                                    generalExpression("sublayoutSize.width -= " +
                                            "measurement.size.width")
                                    ifStatement("layoutParams.width == WRAP_CONTENT") {
                                        codeBlock {
                                            generalExpression("size.width += measurement.size.width")
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
                        }
                        returnStatement("measurement")
                    }
                }
            }
            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size") {
                        functionCallExpression("size.increasedToSize") {
                            argument {
                                initializerExpression("CGSize") {
                                    argument("width", "minWidth ?? 0")
                                    argument("height", "minHeight ?? 0")
                                }
                            }
                        }
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
            variable("sublayoutRect") {
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
                            constant("arrangement") {
                                generalExpression("measurement.arrangement(within: sublayoutRect)")
                            }
                            ifStatement("orientation == .vertical") {
                                codeBlock {
                                    generalExpression("sublayoutRect.origin.y += " +
                                            "measurement.size.height")
                                }
                                elseClause {
                                    codeBlock {
                                        generalExpression("sublayoutRect.origin.x += " +
                                                "measurement.size.width")
                                    }
                                }
                            }
                            returnStatement("arrangement")
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

    clazz("LinearLayoutParams") {
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
