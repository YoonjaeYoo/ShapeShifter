package me.yoonjae.shapeshifter.system

class TextStyle : me.yoonjae.shapeshifter.poet.file.SwiftFile("TextStyle.swift") {
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
