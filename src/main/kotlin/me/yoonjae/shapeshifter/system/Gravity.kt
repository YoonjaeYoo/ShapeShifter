package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class Gravity : SwiftFile("Gravity.swift") {
    init {
        import("UIKit")
        import("LayoutKit")

        struct("Gravity") {
            public()
            superType("OptionSet")

            constant("top", value = "Gravity(rawValue: 1 << 1)") { static() }
            constant("bottom", value = "Gravity(rawValue: 1 << 2)") { static() }
            constant("start", value = "Gravity(rawValue: 1 << 3)") { static() }
            constant("left", value = "Gravity(rawValue: 1 << 3)") { static() }
            constant("end", value = "Gravity(rawValue: 1 << 4)") { static() }
            constant("right", value = "Gravity(rawValue: 1 << 4)") { static() }
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
                        assignmentExpression("vertical", ".bottom")
                        elseIfStatement("contains(.centerVertical) || contains(.center)") {
                            assignmentExpression("vertical", ".center")
                            elseIfStatement("contains(.fillVertical) || contains(.fill)") {
                                assignmentExpression("vertical", ".fill")
                                elseClause {
                                    assignmentExpression("vertical", ".top")
                                }
                            }
                        }
                    }
                    variable("horizontal", Type("Alignment.Horizontal"))
                    ifStatement("contains(.end) || contains(.right)") {
                        assignmentExpression("horizontal", ".trailing")
                        elseIfStatement("contains(.centerHorizontal) || contains(.center)") {
                            assignmentExpression("horizontal", ".center")
                            elseIfStatement("contains(.fillHorizontal) || contains(.fill)") {
                                assignmentExpression("horizontal", ".fill")
                                elseClause {
                                    assignmentExpression("horizontal", ".leading")
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

            variable("contentVerticalAlignment", Type("UIControlContentVerticalAlignment")) {
                codeBlock {
                    ifStatement("contains(.bottom)") {
                        returnStatement(".bottom")
                        elseIfStatement("contains(.centerVertical) || contains(.center)") {
                            returnStatement(".center")
                            elseIfStatement("contains(.fillVertical) || contains(.fill)") {
                                returnStatement(".fill")
                                elseClause {
                                    returnStatement(".top")
                                }
                            }
                        }
                    }
                }
            }

            variable("contentHorizontalAlignment", Type("UIControlContentHorizontalAlignment")) {
                codeBlock {
                    ifStatement("contains(.end) || contains(.right)") {
                        returnStatement(".right")
                        elseIfStatement("contains(.centerHorizontal) || contains(.center)") {
                            returnStatement(".center")
                            elseIfStatement("contains(.fillHorizontal) || contains(.fill)") {
                                returnStatement(".fill")
                                elseClause {
                                    returnStatement(".left")
                                }
                            }
                        }
                    }
                }
            }

            variable("textAlignment", Type("NSTextAlignment")) {
                codeBlock {
                    ifStatement("contains(.end) || contains(.right)") {
                        returnStatement(".right")
                        elseIfStatement("contains(.centerHorizontal) || contains(.center)") {
                            returnStatement(".center")
                            elseIfStatement("contains(.fillHorizontal) || contains(.fill)") {
                                returnStatement(".justified")
                                elseIfStatement("contains(.start) || contains(.left)") {
                                    returnStatement(".left")
                                    elseClause {
                                        returnStatement(".natural")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
