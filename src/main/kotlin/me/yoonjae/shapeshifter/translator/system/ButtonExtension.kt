package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class ButtonExtension : SwiftFile("ButtonExtension.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        extension("Button") {
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
                        argument("background", "background")
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
}
