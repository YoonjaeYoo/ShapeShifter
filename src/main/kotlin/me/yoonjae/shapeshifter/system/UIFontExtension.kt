package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class UIFontExtension : SwiftFile("UIFontExtension.swift") {
    init {
        import("CoreGraphics")
        import("UIKit")

        extension("UIFont") {
            function("font", Type("UIFont")) {
                static()
                parameter("size", Type("CGFloat"), label = "ofSize")
                parameter("style", Type("TextStyle"))

                ifStatement("style == .bold") {
                    returnStatement {
                        functionCallExpression("UIFont.boldSystemFont") {
                            argument("ofSize", "size")
                        }
                    }
                    elseIfStatement("style == .italic") {
                        returnStatement {
                            functionCallExpression("UIFont.italicSystemFont") {
                                argument("ofSize", "size")
                            }
                        }
                        elseClause {
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
