package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val pileLayout = SwiftFile("PileLayout.swift") {
    import("UIKit")
    import("LayoutKit")

    clazz("PileLayout") {
        open()
        genericParameter("V") { superType("View") }
        superType("BaseLayout") { genericParameter("V") }
        superType("ConfigurableLayout")

        constant("sublayouts", type = Type("[Layout]")) { open() }

        initializer {
            public()
            parameter("alignment", Type("Alignment"), ".fill")
            parameter("viewReuseId", Type("String?"), "nil")
            parameter("sublayouts", Type("[Layout]"))
            parameter("config", Type("((V) -> Void)?"), "nil")

            generalExpression("self.sublayouts = sublayouts")
            initializerExpression("super") {
                argument("alignment", "alignment")
                argument("flexibility", ".flexible")
                argument("viewReuseId", "viewReuseId")
                argument("config", "config")
            }
        }

        function("measurement", Type("LayoutMeasurement")) {
            open()
            parameter("maxSize", Type("CGSize"), label = "within")

            constant("size", value = "maxSize")
            returnStatement {
                initializerExpression("LayoutMeasurement") {
                    argument("layout", "self")
                    argument("size", "size")
                    argument("maxSize", "maxSize")
                    argument("sublayouts") {
                        functionCallExpression("sublayouts.map") {
                            trailingClosure {
                                closureParameter("sublayout")
                                functionCallExpression("sublayout.measurement") {
                                    argument("within", "size")
                                }
                            }
                        }
                    }
                }
            }
        }

        function("arrangement", Type("LayoutArrangement")) {
            open()
            parameter("rect", Type("CGRect"), label = "within")
            parameter("measurement", Type("LayoutMeasurement"))

            constant("frame") {
                functionCallExpression("alignment.position") {
                    argument("size", "measurement.size")
                    argument("in", "rect")
                }
            }
            constant("sublayoutRect") {
                initializerExpression("CGRect") {
                    argument("x", "0")
                    argument("y", "0")
                    argument("width", "frame.width")
                    argument("height", "frame.height")
                }
            }
            constant("sublayouts") {
                functionCallExpression("measurement.sublayouts.map") {
                    trailingClosure {
                        closureParameter("measurement")
                        returnStatement {
                            functionCallExpression("measurement.arrangement") {
                                argument("within", "sublayoutRect")
                            }
                        }
                    }
                }
            }
            returnStatement {
                initializerExpression("LayoutArrangement") {
                    argument("layout", "self")
                    argument("frame", "frame")
                    argument("sublayouts", "sublayouts")
                }
            }
        }
    }
}
