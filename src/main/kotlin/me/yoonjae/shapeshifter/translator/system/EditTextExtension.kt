package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class EditTextExtension : SwiftFile("EditTextExtension.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        extension("EditText") {
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
                    parameter("background", Type("UIColor", true), "Color.primary")
                    parameter("gravity", Type("Gravity"), "[.center]")
                    parameter("hint", Type("String", true), "nil")
                    parameter("text", Type("String", true), "nil")
                    parameter("textAppearance", Type("TextAppearance", true), "nil")
                    parameter("textColor", Type("UIColor", true), "Color.white")
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
                        trailingClosure {
                            closureParameter("textField")
                            assignmentExpression("textField.layer.shadowColor",
                                    "theme.colorControlNormal?.cgColor ?? UIColor.white.cgColor")
                            assignmentExpression("textField.tintColor", "theme.colorControlNormal ?? " +
                                    "UIColor.white")
                            functionCallExpression("config?") {
                                argument(null, "textField")
                            }
                        }
                    }
                }
            }
        }
    }
}
