package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.increasedToMinSize


val textView = me.yoonjae.shapeshifter.poet.file.SwiftFile("TextView.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("TextView") {
        public()
        superType("ViewGroup")

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
            parameter("gravity", Type("Gravity"), "[]")
            parameter("lines", Type("Int"), "0")
            parameter("singleLine", Type("Bool"), "false")
            parameter("text", Type("String", true), "nil")
            parameter("textAppearance", Type("TextAppearance", true), "nil")
            parameter("textColor", Type("UIColor", true), "nil")
            parameter("textSize", Type("CGFloat", true), "nil")
            parameter("textStyle", Type("TextStyle", true), "nil")
            parameter("config", Type("(UILabel) -> Void", true), "nil")

            constant("defaultTextAppearance", value = "TextAppearance.AppCompat.Subhead(theme)")
            initializerExpression("super") {
                argument("theme", "theme")
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
                            argument("font") {
                                functionCallExpression("UIFont.font") {
                                    argument("ofSize", "textSize ?? textAppearance?.textSize ?? " +
                                            "defaultTextAppearance.textSize ?? 15")
                                    argument("style",
                                            "textStyle ?? textAppearance?.textStyle ?? " +
                                                    "defaultTextAppearance.textStyle ??" +
                                                    " .normal")
                                }
                            }
                            argument("numberOfLines", "singleLine ? 1 : lines")
                            argument("alignment", "gravity.alignment")
                            argument("flexibility", ".inflexible")
                            argument("viewReuseId", "id")
                            trailingClosure {
                                closureParameter("label")
                                assignmentExpression("label.text", "text")
                                assignmentExpression("label.textColor") {
                                    generalExpression("textColor ?? textAppearance?.textColor ?? " +
                                            "defaultTextAppearance.textColor ?? " +
                                            "UIColor.white")
                                }
                                functionCallExpression("config?") {
                                    argument(null, "label")
                                }
                            }
                        }
                    }
                }
                trailingClosure {
                    closureParameter("view")
                    generalExpression("view.alpha = alpha")
                    generalExpression("view.backgroundColor = background")
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
                            "(layoutParams.width == WRAP_CONTENT ? size.width : " +
                            "layoutParams.width + layoutParams.margin.left + " +
                            "layoutParams.margin.right)")
            assignmentExpression("size.height",
                    "layoutParams.height == MATCH_PARENT ? maxSize.height : " +
                            "(layoutParams.height == WRAP_CONTENT ? size.height : " +
                            "layoutParams.height + layoutParams.margin.top + " +
                            "layoutParams.margin.bottom)")
            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size") {
                        increasedToMinSize()
                    }
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
