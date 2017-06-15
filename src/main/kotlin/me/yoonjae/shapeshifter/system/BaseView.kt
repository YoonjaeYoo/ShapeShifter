package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type

class BaseView : SwiftFile("BaseView.swift") {
    init {
        import("LayoutKit")

        protocol("BaseView") {
            public()
            superType("Layout")

            protocolProperty("theme", Type("Theme")) {
                get()
                set()
            }
            protocolProperty("id", Type("String", true)) {
                get()
                set()
            }
            protocolProperty("layoutParams", Type("LayoutParams")) {
                get()
                set()
            }
            protocolProperty("parent", Type("BaseView", true)) {
                get()
                set()
            }
            protocolMethod("findView", Type("BaseView", true)) {
                parameter("id", Type("String"), label = "by")
            }
            protocolMethod("requestLayout") {
                parameter("view", Type("UIView"), label = "in")
            }
        }
    }
}

