package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.type.Type


val button = me.yoonjae.shapeshifter.poet.file.SwiftFile("Button.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("Button") {
        superType("View") {
            genericParameter("UIButton")
        }

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
            parameter("lines", Type("Int"))
            parameter("singleLine", Type("Bool"))
            parameter("text", Type("String?"), "nil")
            parameter("textAppearance", Type("TextAppearance?"), "nil")
            parameter("textColor", Type("UIColor?"), "nil")
            parameter("textSize", Type("CGFloat"), "15")
            parameter("textStyle", Type("TextStyle"), ".normal")
            parameter("config", Type("((V) -> Void)?"), "nil")

            initializerExpression("super") {
                argument("layoutParams", "layoutParams")
                argument("id", "id")
                argument("padding", "padding")
                argument("minWidth", "minWidth")
                argument("minHeight", "minHeight")
                argument("alpha", "alpha")
                argument("background", "background")
                argument("sublayouts") {
                    arrayLiteralExpression {
                        initializerExpression("LabelLayout<UILabel>") {
                            argument("text", ".unattributed(text == nil ? \"\" : text)")
                            argument("font", "UIFont.font(ofSize: textSize, style: textStyle)")
                            argument("numberOfLines", "singleLine ? 1 : lines")
                            argument("alignment", ".fill")
                            argument("flexibility", ".inflexible")
                            argument("viewReuseId", "viewReuseId")
                            trailingClosure {
                                closureParameter("textView")
                                ifStatement("let background = background") {
                                    codeBlock {
                                        generalExpression("textView.backgroundColor = background")
                                    }
                                }
                                ifStatement("let textColor = textColor") {
                                    codeBlock {
                                        generalExpression("button.setTitleColor(textColor, for: .normal)")
                                    }
                                }
                                functionCallExpression("config?") {
                                    argument(null, "textView")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 }
