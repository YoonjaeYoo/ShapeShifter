package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.type.Type

class UIFontExtension : me.yoonjae.shapeshifter.poet.file.SwiftFile("UIFontExtension.swift") {
    init {
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
}
