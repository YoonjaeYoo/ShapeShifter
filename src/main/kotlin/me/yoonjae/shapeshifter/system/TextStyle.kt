package me.yoonjae.shapeshifter.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile

class TextStyle : SwiftFile("TextStyle.swift") {
    init {
        import("UIKit")
        enum("TextStyle") {
            public()
            case("normal")
            case("bold")
            case("italic")
        }
    }
}
