package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.type.Type


val button = me.yoonjae.shapeshifter.poet.file.SwiftFile("Button.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("Button") {
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
            parameter("gravity", Type("Gravity"), "[.center]")
            parameter("text", Type("String", true), "nil")
            parameter("textAppearance", Type("TextAppearance", true), "nil")
            parameter("textColor", Type("UIColor", true), "nil")
            parameter("textSize", Type("CGFloat", true), "nil")
            parameter("textStyle", Type("TextStyle", true), "nil")
            parameter("config", Type("(UIButton) -> Void", true), "nil")

            constant("defaultTextAppearance", value = "TextAppearance.AppCompat.Subhead(theme)")
            initializerExpression("super") {
                argument("theme", "theme")
                argument("layoutParams", "layoutParams")
                argument("padding", "padding")
                argument("minWidth", "minWidth ?? 88")
                argument("minHeight", "minHeight ?? 44")
                argument("alpha", "alpha")
                argument("background", "background ?? Color.primary")
                argument("sublayouts") {
                    arrayLiteralExpression {
                        initializerExpression("ButtonLayout<UIButton>") {
                            argument("type", ".system")
                            argument("title", "text == nil ? \"\" : text!")
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
                            argument("alignment", "gravity.alignment")
                            argument("flexibility", ".inflexible")
                            argument("viewReuseId", "id")
                            trailingClosure {
                                closureParameter("button")
                                ifStatement("let text = text") {
                                    codeBlock {
                                        functionCallExpression("button.setTitle") {
                                            argument(null, "text")
                                            argument("for", ".normal")
                                        }
                                    }
                                }
                                functionCallExpression("button.setTitleColor") {
                                    argument(null, "textColor ?? textAppearance?.textColor ?? " +
                                            "UIColor.white")
                                    argument("for", ".normal")
                                }
                                functionCallExpression("config?") {
                                    argument(null, "button")
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

        clazz("Inverse") {
            superType("Button")

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
                parameter("text", Type("String", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UIButton) -> Void", true), "nil")

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
                    argument("text", "text")
                    argument("textAppearance", "textAppearance")
                    argument("textColor", "textColor ?? Color.primary")
                    argument("textSize", "textSize")
                    argument("textStyle", "textStyle")
                    argument("config", "config")
                }
            }
        }

        clazz("Transparent") {
            superType("Button")

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
                parameter("text", Type("String", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UIButton) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background ?? Color.transparent")
                    argument("gravity", "gravity")
                    argument("text", "text")
                    argument("textAppearance", "textAppearance")
                    argument("textColor", "textColor ?? Color.primary")
                    argument("textSize", "textSize")
                    argument("textStyle", "textStyle")
                    argument("config", "config")
                }
            }
        }

        clazz("Tutorial") {
            superType("Transparent")

            initializer {
                override()
                public()
                parameter("theme", Type("Theme"), "AppTheme()")
                parameter("id", Type("String", true), "nil")
                parameter("layoutParams", Type("LayoutParams"))
                parameter("padding", Type("UIEdgeInsets")) {
                    initializerExpression("UIEdgeInsets") {
                        argument(value = "Dimen.spacingSmall")
                    }
                }
                parameter("minWidth", Type("CGFloat", true), "nil")
                parameter("minHeight", Type("CGFloat", true), "nil")
                parameter("alpha", Type("CGFloat"), "1.0")
                parameter("background", Type("UIColor", true), "nil")
                parameter("gravity", Type("Gravity"), "[.center]")
                parameter("text", Type("String", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UIButton) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background ?? Color.transparent")
                    argument("gravity", "gravity")
                    argument("text", "text")
                    argument("textAppearance", "textAppearance")
                    argument("textColor", "textColor ?? Color.primary")
                    argument("textSize", "textSize")
                    argument("textStyle", "textStyle")
                    argument("config", "config")
                }
            }
        }

        clazz("Modify") {
            superType("Transparent")

            initializer {
                override()
                public()
                parameter("theme", Type("Theme"), "AppTheme()")
                parameter("id", Type("String", true), "nil")
                parameter("layoutParams", Type("LayoutParams"))
                parameter("padding", Type("UIEdgeInsets")) {
                    initializerExpression("UIEdgeInsets")
                }
                parameter("minWidth", Type("CGFloat", true), "0")
                parameter("minHeight", Type("CGFloat", true), "0")
                parameter("alpha", Type("CGFloat"), "1.0")
                parameter("background", Type("UIColor", true), "nil")
                parameter("gravity", Type("Gravity"), "[.center]")
                parameter("text", Type("String", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UIButton) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding") {
                        initializerExpression("UIEdgeInsets") {
                            argument(value = "Dimen.spacingNormal")
                        }
                    }
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background ?? Color.transparent")
                    argument("gravity", "gravity")
                    argument("text", "text ?? \"modify\".localized()")
                    argument("textAppearance",
                            "textAppearance ?? TextAppearance.AppCompat.Body1(theme)")
                    argument("textColor", "textColor ?? Color.blue")
                    argument("textSize", "textSize")
                    argument("textStyle", "textStyle")
                    argument("config", "config")
                }
            }
        }
    }
}

