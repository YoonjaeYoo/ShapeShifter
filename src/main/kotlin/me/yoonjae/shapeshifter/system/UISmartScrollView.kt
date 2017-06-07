package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class UISmartScrollView : SwiftFile("UISmartScrollView.swift") {
    init {
        import("UIKit")

        clazz("UISmartScrollView") {
            superType("UIScrollView") {
                function("didAddSubview") {
                    override()
                    parameter("subview", Type("UIView"), label = "_")

                    functionCallExpression("super.didAddSubview") {
                        argument(value = "subview")
                    }
                    constant("contentRect") {
                        initializerExpression("CGRect") {
                            argument("origin", "CGPoint()")
                            argument("size", "contentSize")
                        }
                    }
                    assignmentExpression("contentSize", "contentRect.union(subview.frame).size")
                }
            }
        }
    }
}

