package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.increasedToMinSize


val editText = me.yoonjae.shapeshifter.poet.file.SwiftFile("EditText.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("EditText") {
        public()
        superType("View<UITextField>")
        constant("text", Type("String", true))
        constant("font", Type("UIFont"))

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
            parameter("gravity", Type("Gravity"), "[.center]")
            parameter("hint", Type("String", true), "nil")
            parameter("text", Type("String", true), "nil")
            parameter("textAppearance", Type("TextAppearance", true), "nil")
            parameter("textColor", Type("UIColor", true), "nil")
            parameter("textSize", Type("CGFloat", true), "nil")
            parameter("textStyle", Type("TextStyle", true), "nil")
            parameter("config", Type("(UITextField) -> Void", true), "nil")

            constant("defaultTextAppearance", value = "TextAppearance.AppCompat.Subhead(theme)")
            assignmentExpression("self.text", "text")
            assignmentExpression("self.font") {
                functionCallExpression("UIFont.font") {
                    argument("ofSize", "textSize ?? textAppearance?.textSize ?? " +
                            "defaultTextAppearance.textSize ?? 15")
                    argument("style",
                            "textStyle ?? textAppearance?.textStyle ?? " +
                                    "defaultTextAppearance.textStyle ??" +
                                    " .normal")
                }
            }
            initializerExpression("super") {
                argument("theme", "theme")
                argument("layoutParams", "layoutParams")
                argument("padding", "padding")
                argument("minWidth", "minWidth")
                argument("minHeight", "minHeight")
                argument("alpha", "alpha")
                argument("background", "background ?? Color.primary")
                trailingClosure {
                    closureParameter("textField")
                    generalExpression("textField.alpha = alpha")
                    generalExpression("textField.backgroundColor = background")
                    assignmentExpression("textField.leftView") {
                        initializerExpression("UIView") {
                            argument("frame") {
                                initializerExpression("CGRect") {
                                    argument("origin", "CGPoint()")
                                    argument("size", "CGSize(width: padding.left, " +
                                            "height: 0)")
                                }
                            }
                        }
                    }
                    assignmentExpression("textField.leftViewMode", ".always")
                    assignmentExpression("textField.rightView") {
                        initializerExpression("UIView") {
                            argument("frame") {
                                initializerExpression("CGRect") {
                                    argument("origin", "CGPoint()")
                                    argument("size", "CGSize(width: padding.right, " +
                                            "height: 0)")
                                }
                            }
                        }
                    }
                    assignmentExpression("textField.rightViewMode", ".always")
                    assignmentExpression("textField.contentVerticalAlignment",
                            "gravity.contentVerticalAlignment")
                    assignmentExpression("textField.contentHorizontalAlignment",
                            "gravity.contentHorizontalAlignment")
                    assignmentExpression("textField.placeholder", "hint")
                    assignmentExpression("textField.text", "text")
                    assignmentExpression("textField.textColor", "textColor ?? " +
                            "textAppearance?.textColor ?? UIColor.white")
                    functionCallExpression("config?") {
                        argument(null, "textField")
                    }
                }
            }
        }

        function("measurement", Type("LayoutMeasurement")) {
            override()
            public()
            parameter("maxSize", Type("CGSize"), label = "within")

            variable("rect", Type("CGRect"), "CGRect()")
            ifStatement("let text = self.text") {
                codeBlock {
                    assignmentExpression("rect") {
                        functionCallExpression("text.boundingRect") {
                            argument("with", value = "maxSize")
                            argument("options", value = "[.usesLineFragmentOrigin]")
                            argument("attributes", value = "[NSFontAttributeName: self.font]")
                            argument("context", value = "nil")
                        }
                    }
                }
            }
            variable("size", value = "rect.size.increasedByInsets(padding)" +
                    ".increasedByInsets(layoutParams.margin)")
            assignmentExpression("size.height", "size.height.roundedUpToFractionalPoint")
            assignmentExpression("size.width", "size.width.roundedUpToFractionalPoint")
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
                    argument("sublayouts", "[]")
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
            returnStatement {
                functionCallExpression("LayoutArrangement") {
                    argument("layout", "self")
                    argument("frame", "frame")
                    argument("sublayouts", "[]")
                }
            }
        }

        clazz("Inverse") {
            superType("EditText")

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
                parameter("gravity", Type("Gravity"), "[.center]")
                parameter("hint", Type("String", true), "nil")
                parameter("text", Type("String", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UITextField) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background ?? Color.white")
                    argument("gravity", "gravity")
                    argument("hint", "hint")
                    argument("text", "text")
                    argument("textAppearance", "textAppearance")
                    argument("textColor", "textColor ?? Color.primary")
                    argument("textSize", "textSize")
                    argument("textStyle", "textStyle")
                    argument("config", "config")
                }
            }
        }
    }
}

