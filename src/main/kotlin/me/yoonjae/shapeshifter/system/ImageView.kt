package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.translator.increasedToMinSize


class ImageView : me.yoonjae.shapeshifter.poet.file.SwiftFile("ImageView.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        clazz("ImageView") {
            public()
            superType("ViewGroup")

            constant("scaleType", me.yoonjae.shapeshifter.poet.type.Type("ScaleType")) { public() }
            constant("src", me.yoonjae.shapeshifter.poet.type.Type("UIImage", true)) { public() }

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
                parameter("scaleType", me.yoonjae.shapeshifter.poet.type.Type("ScaleType"), ".fitCenter")
                parameter("src", me.yoonjae.shapeshifter.poet.type.Type("UIImage", true), "nil")
                parameter("config", me.yoonjae.shapeshifter.poet.type.Type("(UIImageView) -> Void", true), "nil")

                assignmentExpression("self.scaleType", "scaleType")
                assignmentExpression("self.src", "src")
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
                            initializerExpression("View<UIImageView>") {
                                argument("theme", "theme")
                                argument("layoutParams") {
                                    initializerExpression("LayoutParams") {
                                        argument("width", "MATCH_PARENT")
                                        argument("height", "MATCH_PARENT")
                                        argument("margin", "padding")
                                    }
                                }
                                trailingClosure {
                                    closureParameter("imageView")
                                    ifStatement("scaleType == .center") {
                                        codeBlock {
                                            assignmentExpression("imageView.contentMode", ".center")
                                        }
                                        elseClause {
                                            ifStatement("scaleType == .centerCrop") {
                                                codeBlock {
                                                    assignmentExpression("imageView.contentMode",
                                                            ".scaleAspectFill")
                                                }
                                                elseClause {
                                                    ifStatement("scaleType == .fitCenter") {
                                                        codeBlock {
                                                            assignmentExpression("imageView.contentMode",
                                                                    ".scaleAspectFit")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    assignmentExpression("imageView.image", "src")
                                    functionCallExpression("config?") {
                                        argument(null, "imageView")
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

                constant("srcSize", value = "self.src == nil ? CGSize() : self.src!.size")
                constant("size") {
                    functionCallExpression("maxSize.decreasedToSize") {
                        argument {
                            functionCallExpression("CGSize") {
                                argument("width",
                                        "layoutParams.width == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.width == WRAP_CONTENT ? " +
                                                "srcSize.width + layoutParams.margin.left + " +
                                                "layoutParams.margin.right : layoutParams.width + " +
                                                "layoutParams.margin.left + layoutParams.margin.right)")
                                argument("height",
                                        "layoutParams.height == MATCH_PARENT ? .greatestFiniteMagnitude : " +
                                                "(layoutParams.width == WRAP_CONTENT ? " +
                                                "srcSize.height + layoutParams.margin.top + " +
                                                "layoutParams.margin.bottom : layoutParams.height + " +
                                                "layoutParams.margin.top + layoutParams.margin.bottom)")
                            }
                        }
                    }
                }
                constant("measurement", value = "children[0].measurement(within: " +
                        "size.decreasedByInsets(layoutParams.margin).decreasedByInsets(padding))")
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
                constant("arrangement") {
                    functionCallExpression("measurement.sublayouts[0].arrangement") {
                        argument("within") {
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
                    }
                }
                returnStatement {
                    functionCallExpression("LayoutArrangement") {
                        argument("layout", "self")
                        argument("frame", "frame")
                        argument("sublayouts", "[arrangement]")
                    }
                }
            }

            enum("ScaleType") {
                public()
                case("center")
                case("centerCrop")
                case("fitCenter")
            }
        }
    }
}
