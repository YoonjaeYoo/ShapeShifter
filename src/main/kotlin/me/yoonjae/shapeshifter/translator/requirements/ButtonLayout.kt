package me.yoonjae.shapeshifter.translator.requirements

import me.yoonjae.shapeshifter.poet.type.Type


val buttonLayout = me.yoonjae.shapeshifter.poet.file.SwiftFile("ButtonLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    constant("defaultAlignment", "Alignment.topLeading") { private() }
    constant("defaultFlexibility", "Flexibility.flexible") { private() }

    clazz("ButtonLayout") {
        superType("LayoutKit.ButtonLayout") {
            genericParameter("UIButton")
        }

        initializer {
            parameter("type", Type("ButtonLayoutType"), ".custom")
            parameter("title", Type("String"))
            parameter("image", Type("ButtonLayoutImage"), ".defaultImage")
            parameter("font", Type("UIFont?"), "System.Font.button")
            parameter("textColor", Type("UIColor?"))
            parameter("backgroundColor", Type("UIColor?"))
            parameter("contentEdgeInsets", Type("EdgeInsets")) {
                initializerExpression("EdgeInsets") {
                    argument("top", "Dimen.spacingSmall")
                    argument("left", "Dimen.spacingNormal")
                    argument("bottom", "Dimen.spacingSmall")
                    argument("right", "Dimen.spacingNormal")
                }
            }
            parameter("alignment", Type("Alignment"), "defaultAlignment")
            parameter("flexibility", Type("Flexibility"), "defaultFlexibility")
            parameter("viewReuseId", Type("String?"), "nil")
            parameter("config", Type("((UIButton) -> Void)?"), "nil")

            initializerExpression("super") {
                argument("type", "type")
                argument("title", "title")
                argument("image", "image")
                argument("contentEdgeInsets", "contentEdgeInsets")
                argument("viewReuseId", "viewReuseId")
                argument("config") {
                    closureExpression {
                        closureParameter("button")
                        ifStatement("let color = backgroundColor") {
                            generalExpression("button.backgroundColor = color")
                        }
                        ifStatement("let color = textColor") {
                            generalExpression("button.setTitleColor(color, for: .normal)")
                        }
                        functionCallExpression("config?") {
                            argument(null, "button")
                        }
                    }
                }
            }
        }

        function("normal", Type("ButtonLayout")) {
            clazz()
            parameter("title", Type("String"))
            parameter("image", Type("ButtonLayoutImage"), ".defaultImage")
            parameter("alignment", Type("Alignment"), "defaultAlignment")
            parameter("flexibility", Type("Flexibility"), "defaultFlexibility")
            parameter("viewReuseId", Type("String?"), "nil")
            parameter("config", Type("((UIButton) -> Void)?"), "nil")

            returnStatement {
                functionCallExpression("ButtonLayout") {
                    argument("title", "title")
                    argument("image", "image")
                    argument("font", "System.Font.button")
                    argument("textColor", "Color.white")
                    argument("backgroundColor", "Color.primary")
                    argument("alignment", "alignment")
                    argument("flexibility", "flexibility")
                    argument("viewReuseId", "viewReuseId")
                    argument("config", "config")
                }
            }
        }
    }
}
