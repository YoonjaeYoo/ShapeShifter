package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

val uiFontExtension = SwiftFile("UIFontExtension.swift") {
    import("CoreGraphics")
    import("UIKit")

    extension("UIFont") {
        function("font", Type("UIFont")) {
            static()
            parameter("size", Type("CGFloat"), label = "ofSize")
            parameter("style", Type("TextStyle"))

            ifStatement("style == .bold") {
                codeBlock {
                    returnStatement {
                        functionCallExpression("UIFont.boldSystemFont") {
                            argument("ofSize", "size")
                        }
                    }
                }
                elseClause {
                    ifStatement("style == .italic") {
                        codeBlock {
                            returnStatement {
                                functionCallExpression("UIFont.italicSystemFont") {
                                    argument("ofSize", "size")
                                }
                            }
                        }
                        elseClause {
                            codeBlock {
                                returnStatement {
                                    functionCallExpression("UIFont.systemFont") {
                                        argument("ofSize", "size")
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
