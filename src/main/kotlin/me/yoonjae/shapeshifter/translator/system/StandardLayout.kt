package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val standardLayout = SwiftFile("StandardLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("StandardLayout") {
        open()
        genericParameter("V") {
            superType("View")
        }
        superType("BaseLayout") {
            genericParameter("V")
        }
        superType("ConfigurableLayout")

//        constant("theme", Type("Theme")) { open() }
        constant("width", Type("CGFloat?")) { open() }
        constant("height", Type("CGFloat?")) { open() }
        constant("margin", Type("EdgeInsets")) { open() }
        constant("sublayout", Type("Layout?")) { open() }

        initializer {
            public()
//            parameter("theme", Type("Theme"), "Theme.light()")
            parameter("width", Type("CGFloat?"), "nil")
            parameter("height", Type("CGFloat?"), "nil")
            parameter("margin", Type("EdgeInsets")) {
                initializerExpression("EdgeInsets")
            }
            parameter("alignment", Type("Alignment"), ".fill")
            parameter("flexibility", Type("Flexibility"), ".flexible")
            parameter("viewReuseId", Type("String?"), "nil")
            parameter("sublayout", Type("Layout?"), "nil")
            parameter("config", Type("((V) -> Void)?"), "nil")

            assignmentExpression("self.width", "width")
            assignmentExpression("self.height", "height")
            assignmentExpression("self.margin", "margin")
            assignmentExpression("self.sublayout", "sublayout")
            initializerExpression("super") {
                argument("alignment", "alignment")
                argument("flexibility", "flexibility")
                argument("viewReuseId", "viewReuseId")
                argument("config", "config")
            }
        }

        function("measurement", Type("LayoutMeasurement")) {
            public()
            parameter("maxSize", Type("CGSize"), label = "within")

            variable("size") {
                functionCallExpression("maxSize.decreasedToSize") {
                    argument {
                        functionCallExpression("CGSize") {
                            argument("width", "self.width ?? .greatestFiniteMagnitude")
                            argument("height", "self.height ?? .greatestFiniteMagnitude")
                        }
                    }
                }
            }
            variable("sublayoutMeasurement", Type("LayoutMeasurement?"), "nil")
            ifStatement("let sublayout = sublayout") {
                assignmentExpression("sublayoutMeasurement",
                        "sublayout.measurement(within: maxSize.decreasedByInsets(margin))")
                assignmentExpression("size", "sublayoutMeasurement!.size.increasedByInsets(margin)")
            }
            returnStatement {
                functionCallExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size", "size")
                    argument("maxSize", "maxSize")
                    argument("sublayouts",
                            "sublayoutMeasurement == nil ? [] : [sublayoutMeasurement!]")
                }
            }
        }

        function("arrangement", Type("LayoutArrangement")) {
            public()
            parameter("rect", Type("CGRect"), label = "within")
            parameter("measurement", Type("LayoutMeasurement"))

            constant("frame", value = "alignment.position(size: measurement.size, in: rect)")
            variable("sublayouts", Type("[LayoutArrangement]"), "[]")
            constant("marginOrigin", value = "CGPoint(x: margin.left, y: margin.top)")
            constant("marginSize", value = "frame.size.decreasedByInsets(margin)")
            constant("sublayoutRect", value = "CGRect(origin: marginOrigin, size: marginSize)")
            assignmentExpression("sublayouts") {
                functionCallExpression("measurement.sublayouts.map") {
                    trailingClosure {
                        closureParameter("measurement") {
                            returnStatement {
                                generalExpression("measurement.arrangement(within: sublayoutRect)")
                            }
                        }
                    }
                }
            }
            returnStatement {
                functionCallExpression("LayoutArrangement") {
                    argument("layout", "self")
                    argument("frame", "frame")
                    argument("sublayouts", "sublayouts")
                }
            }
        }
    }
}
