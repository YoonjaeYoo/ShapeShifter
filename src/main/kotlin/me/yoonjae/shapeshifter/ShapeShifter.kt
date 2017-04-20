package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.ColorsTranslator

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {
    companion object {
        var indentWidth: Int = 4
    }

    fun shift() {
        listOf(ColorsTranslator(androidAppDir, iosAppDir)).forEach { it.translate() }
    }
}