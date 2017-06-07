package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize


class TextView : SwiftFile("TextView.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("TextView") {
            public()
            superType("ViewGroup")

            variable("gravity", Type("Gravity"), "[]")
            variable("lines", Type("Int"), "0")
            variable("singleLine", Type("Bool"), "false")
            variable("text", Type("String", true), "nil")
            variable("attributedText", Type("NSAttributedString", true), "nil")
            variable("textAppearance", Type("TextAppearance", true), "nil")
            variable("textColor", Type("UIColor", true), "nil")
            variable("textSize", Type("CGFloat", true), "nil")
            variable("textStyle", Type("TextStyle", true), "nil")

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
                parameter("attributedText", Type("NSAttributedString", true), "nil")
                parameter("textAppearance", Type("TextAppearance", true), "nil")
                parameter("textColor", Type("UIColor", true), "nil")
                parameter("textSize", Type("CGFloat", true), "nil")
                parameter("textStyle", Type("TextStyle", true), "nil")
                parameter("config", Type("(UILabel) -> Void", true), "nil")

                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("id", "id")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                }
                assignmentExpression("self.gravity", "gravity")
                assignmentExpression("self.lines", "lines")
                assignmentExpression("self.singleLine", "singleLine")
                assignmentExpression("self.text", "text")
                assignmentExpression("self.attributedText", "attributedText")
                assignmentExpression("self.textAppearance", "textAppearance")
                assignmentExpression("self.textColor", "textColor")
                assignmentExpression("self.textSize", "textSize")
                assignmentExpression("self.textStyle", "textStyle")
                assignmentExpression("self.config") {
                    closureExpression {
                        closureParameter("label")

                        assignmentExpression("label.alpha", "self.alpha")
                        assignmentExpression("label.backgroundColor", "self.background")
                        assignmentExpression("label.numberOfLines",
                                "self.singleLine ? 1 : self.lines")
                        assignmentExpression("label.textAlignment", "self.gravity.textAlignment")
                        assignmentExpression("label.text", "self.text")
                        assignmentExpression("label.textColor") {
                            generalExpression("self.textColor ?? textAppearance?.textColor ?? " +
                                    "defaultTextAppearance.textColor ?? " +
                                    "theme.textColorPrimary ?? UIColor.black")
                        }
                        assignmentExpression("label.font", "self.calculateFont()")
                        functionCallExpression("config?") {
                            argument(null, "label")
                        }
                    }
                }
            }

            function("measurement", Type("LayoutMeasurement")) {
                override()
                public()
                parameter("maxSize", Type("CGSize"), label = "within")

                constant("font", value = "calculateFont()")
                constant("rect") {
                    functionCallExpression("(self.text ?? \" \").boundingRect") {
                        argument("with", "maxSize")
                        argument("options", "[.usesLineFragmentOrigin]")
                        argument("attributes", "[NSFontAttributeName: font]")
                        argument("context", "nil")
                    }
                }
                variable("size", value = "rect.size.increasedByInsets(padding).increasedByInsets(layoutParams.margin)")
                assignmentExpression("size.width", "size.width.roundedUpToFractionalPoint")
                assignmentExpression("size.height", "size.height.roundedUpToFractionalPoint")
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

            function("calculateFont", Type("UIFont")) {
                private()

                returnStatement {
                    functionCallExpression("UIFont.font") {
                        argument("ofSize", "textSize ?? textAppearance?.textSize ?? defaultTextAppearance().textSize ?? 15")
                        argument("style", "textStyle ?? textAppearance?.textStyle ?? defaultTextAppearance().textStyle ?? .normal")
                    }
                }
            }

            function("defaultTextAppearance", Type("TextAppearance")) {
                private()

                returnStatement("TextAppearance.AppCompat.Subhead(theme)")
            }
        }
    }
}
