package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val gravity = SwiftFile("Gravity.swift") {
    import("UIKit")
    import("LayoutKit")

    struct("Gravity") {
        public()
        superType("OptionSet")

        constant("top", value = "Gravity(rawValue: 1 << 1)") { static() }
        constant("bottom", value = "Gravity(rawValue: 1 << 2)") { static() }
        constant("start", value = "Gravity(rawValue: 1 << 3)") { static() }
        constant("end", value = "Gravity(rawValue: 1 << 4)") { static() }
        constant("centerVertical", value = "Gravity(rawValue: 1 << 5)") { static() }
        constant("centerHorizontal", value = "Gravity(rawValue: 1 << 6)") { static() }
        constant("center", value = "Gravity(rawValue: 1 << 7)") { static() }
        constant("fillVertical", value = "Gravity(rawValue: 1 << 8)") { static() }
        constant("fillHorizontal", value = "Gravity(rawValue: 1 << 9)") { static() }
        constant("fill", value = "Gravity(rawValue: 1 << 10)") { static() }
        constant("rawValue", Type("Int")) { public() }

        function("==", Type("Bool")) {
            public()
            static()
            parameter("lhs", Type("Gravity"))
            parameter("rhs", Type("Gravity"))

            returnStatement("lhs.rawValue == rhs.rawValue")
        }

        initializer {
            public()
            parameter("rawValue", Type("Int"))
            assignmentExpression("self.rawValue", "rawValue")
        }

        variable("alignment", Type("Alignment")) {
            codeBlock {
                variable("vertical", Type("Alignment.Vertical"))
                ifStatement("contains(.bottom)") {
                    codeBlock {
                        assignmentExpression("vertical", ".bottom")
                    }
                    elseClause {
                        ifStatement("contains(.centerVertical) || contains(.center)") {
                            codeBlock {
                                assignmentExpression("vertical", ".center")
                            }
                            elseClause {
                                ifStatement("contains(.fillVertical) || contains(.fill)") {
                                    codeBlock {
                                        assignmentExpression("vertical", ".fill")
                                    }
                                    elseClause {
                                        codeBlock {
                                            assignmentExpression("vertical", ".top")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                variable("horizontal", Type("Alignment.Horizontal"))
                ifStatement("contains(.bottom)") {
                    codeBlock {
                        assignmentExpression("horizontal", ".trailing")
                    }
                    elseClause {
                        ifStatement("contains(.centerHorizontal) || contains(.center)") {
                            codeBlock {
                                assignmentExpression("horizontal", ".center")
                            }
                            elseClause {
                                ifStatement("contains(.fillHorizontal) || contains(.fill)") {
                                    codeBlock {
                                        assignmentExpression("horizontal", ".fill")
                                    }
                                    elseClause {
                                        codeBlock {
                                            assignmentExpression("horizontal", ".leading")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                returnStatement {
                    initializerExpression("Alignment") {
                        argument("vertical", "vertical")
                        argument("horizontal", "horizontal")
                    }
                }
            }
        }
    }
}
