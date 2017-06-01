package me.yoonjae.shapeshifter.system

import jdk.nashorn.internal.objects.NativeFunction.function
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.extensions.increasedToMinSize


class TextView : SwiftFile("TextView.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("TextView") {
            public()
            superType("ViewGroup")

            initializer {
                public()
                parameter("theme", me.yoonjae.shapeshifter.poet.type.Type("Theme"), "AppTheme()")
                parameter("id", me.yoonjae.shapeshifter.poet.type.Type("String", true), "nil")
                parameter("layoutParams", me.yoonjae.shapeshifter.poet.type.Type("LayoutParams"))
                parameter("padding", me.yoonjae.shapeshifter.poet.type.Type("UIEdgeInsets")) {
                    initializerExpression("UIEdgeInsets")
                }
                parameter("minWidth", me.yoonjae.shapeshifter.poet.type.Type("CGFloat", true), "nil")
                parameter("minHeight", me.yoonjae.shapeshifter.poet.type.Type("CGFloat", true), "nil")
                parameter("alpha", me.yoonjae.shapeshifter.poet.type.Type("CGFloat"), "1.0")
                parameter("background", me.yoonjae.shapeshifter.poet.type.Type("UIColor", true), "nil")
                parameter("gravity", me.yoonjae.shapeshifter.poet.type.Type("Gravity"), "[]")
                parameter("lines", me.yoonjae.shapeshifter.poet.type.Type("Int"), "0")
                parameter("singleLine", me.yoonjae.shapeshifter.poet.type.Type("Bool"), "false")
                parameter("text", me.yoonjae.shapeshifter.poet.type.Type("String", true), "nil")
                parameter("textAppearance", me.yoonjae.shapeshifter.poet.type.Type("TextAppearance", true), "nil")
                parameter("textColor", me.yoonjae.shapeshifter.poet.type.Type("UIColor", true), "nil")
                parameter("textSize", me.yoonjae.shapeshifter.poet.type.Type("CGFloat", true), "nil")
                parameter("textStyle", me.yoonjae.shapeshifter.poet.type.Type("TextStyle", true), "nil")
                parameter("config", me.yoonjae.shapeshifter.poet.type.Type("(UILabel) -> Void", true), "nil")

                constant("defaultTextAppearance", value = "TextAppearance.AppCompat.Subhead(theme)")
                initializerExpression("super") {
                    argument("theme", "theme")
                    argument("layoutParams", "layoutParams")
                    argument("padding", "padding")
                    argument("minWidth", "minWidth")
                    argument("minHeight", "minHeight")
                    argument("alpha", "alpha")
                    argument("background", "background")
                    argument("children") {
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
                                    assignmentExpression("label.alpha", "alpha")
                                    assignmentExpression("label.backgroundColor", "background")
                                    assignmentExpression("label.text", "text")
                                    assignmentExpression("label.textColor") {
                                        generalExpression("textColor ?? textAppearance?.textColor ?? " +
                                                "defaultTextAppearance.textColor ?? " +
                                                "theme.textColorPrimary ?? UIColor.black")
                                    }
                                    functionCallExpression("config?") {
                                        argument(null, "label")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            function("measurement", me.yoonjae.shapeshifter.poet.type.Type("LayoutMeasurement")) {
                override()
                public()
                parameter("maxSize", me.yoonjae.shapeshifter.poet.type.Type("CGSize"), label = "within")

                constant("measurement") {
                    functionCallExpression("children[0].measurement") {
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

            function("arrangement", me.yoonjae.shapeshifter.poet.type.Type("LayoutArrangement")) {
                override()
                public()
                parameter("rect", me.yoonjae.shapeshifter.poet.type.Type("CGRect"), label = "within")
                parameter("measurement", me.yoonjae.shapeshifter.poet.type.Type("LayoutMeasurement"))

                constant("frame") {
                    functionCallExpression("layoutParams.arrangement") {
                        argument("size", "measurement.size")
                        argument("in", "rect")
                    }
                }
                constant("childRect") {
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
                constant("arrangements", me.yoonjae.shapeshifter.poet.type.Type("[LayoutArrangement]")) {
                    functionCallExpression("measurement.sublayouts.map") {
                        trailingClosure {
                            closureParameter("measurement") {
                                returnStatement("measurement.arrangement(within: childRect)")
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
}
