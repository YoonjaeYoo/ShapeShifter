package me.yoonjae.shapeshifter.translator.system

import me.yoonjae.shapeshifter.poet.file.SwiftFile

val drawable = SwiftFile("Drawable.swift") {
    import("UIKit")

    enum("Drawable") {
        case("color") {
            type("UIColor")
        }
        case("image") {
            type("UIImage", true)
        }
    }
}

