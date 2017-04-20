package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.ColorsTranslator
import me.yoonjae.shapeshifter.translator.StringsTranslator

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {
    companion object {
        var indentWidth: Int = 4
    }

    fun shift() {
        listOf(ColorsTranslator(androidAppDir, iosAppDir),
                StringsTranslator(androidAppDir, iosAppDir)).forEach { it.translate() }
    }
}