package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile

val textStyle = SwiftFile("TextStyle.swift") {
    import("UIKit")
    enum("TextStyle") {
        case("normal")
        case("bold")
        case("italic")
    }
}