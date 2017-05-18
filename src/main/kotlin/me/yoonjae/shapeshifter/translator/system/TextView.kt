package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.type.Type


val textView = me.yoonjae.shapeshifter.poet.file.SwiftFile("TextView.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("TextView") {
        superType("ViewGroup")

        initializer {
            parameter("layoutParams", Type("LayoutParams"))
            parameter("id", Type("String?"), "nil")
            parameter("padding", Type("UIEdgeInsets")) {
                initializerExpression("UIEdgeInsets")
            }
            parameter("minWidth", Type("CGFloat?"), "nil")
            parameter("minHeight", Type("CGFloat?"), "nil")
            parameter("alpha", Type("CGFloat"), "1.0")
            parameter("background", Type("UIColor?"), "nil")
            parameter("gravity", Type("Gravity"), "[]")
            parameter("lines", Type("Int"), "0")
            parameter("singleLine", Type("Bool"), "false")
            parameter("text", Type("String?"), "nil")
            parameter("textAppearance", Type("TextAppearance?"), "nil")
            parameter("textColor", Type("UIColor"), "Theme.light().textColorPrimary")
            parameter("textSize", Type("CGFloat"), "15")
            parameter("textStyle", Type("TextStyle"), ".normal")
            parameter("config", Type("((UILabel) -> Void)?"), "nil")

            initializerExpression("super") {
                argument("layoutParams", "layoutParams")
                argument("padding", "padding")
                argument("minWidth", "minWidth")
                argument("minHeight", "minHeight")
                argument("alpha", "alpha")
                argument("background", "background")
                argument("sublayouts") {
                    arrayLiteralExpression {
                        initializerExpression("LabelLayout<UILabel>") {
                            argument("text", ".unattributed(text == nil ? \"\" : text!)")
                            argument("font", "UIFont.font(ofSize: textSize, style: textStyle)")
                            argument("numberOfLines", "singleLine ? 1 : lines")
                            argument("alignment", "gravity.alignment")
                            argument("flexibility", ".inflexible")
                            argument("viewReuseId", "id")
                            trailingClosure {
                                closureParameter("label")
                                ifStatement("let text = text") {
                                    codeBlock {
                                        assignmentExpression("label.text", "text")
                                    }
                                }
                                assignmentExpression("label.textColor", "textColor")
                                functionCallExpression("config?") {
                                    argument(null, "label")
                                }
                            }
                        }
                    }
                }
                trailingClosure {
                    closureParameter("view")
                    ifStatement("let background = background") {
                        codeBlock {
                            generalExpression("view.backgroundColor = background")
                        }
                    }
                }
            }
        }

        function("measurement", Type("LayoutMeasurement")) {
            override()
            public()
            parameter("maxSize", Type("CGSize"), label = "within")

            constant("measurement") {
                functionCallExpression("sublayouts[0].measurement") {
                    argument("within", "maxSize.decreasedByInsets(layoutParams.margin)")
                }
            }
            variable("size", value = "measurement.size.increasedByInsets(layoutParams.margin)")
            assignmentExpression("size.width",
                    "layoutParams.width == MATCH_PARENT ? maxSize.width : " +
                            "(layoutParams.width == WRAP_CONTENT ? size.width : layoutParams.width)")
            assignmentExpression("size.height",
                    "layoutParams.height == MATCH_PARENT ? maxSize.height : " +
                            "(layoutParams.height == WRAP_CONTENT ? size.height : layoutParams.height)")
            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size", "size")
                    argument("maxSize", "maxSize")
                    argument("sublayouts", "[measurement]")
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
    }
}
