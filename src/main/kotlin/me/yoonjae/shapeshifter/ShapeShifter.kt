package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.poet.toIosResourceName
import me.yoonjae.shapeshifter.translator.ColorsTranslator
import me.yoonjae.shapeshifter.translator.DimensTranslator
import me.yoonjae.shapeshifter.translator.LayoutTranslator
import me.yoonjae.shapeshifter.translator.StringsTranslator
import java.io.File
import java.io.FileWriter

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    fun shift() {
        shiftStrings()
        shiftColors()
        shiftDimens()
        shiftLayouts()
    }

    private fun shiftStrings() {
        val input = File(androidAppDir + "/src/main/res/values/strings.xml")
        val output = File(iosAppDir + "/ko.lproj/Localizable.strings")
        output.createWithParent()
        FileWriter(output).use { StringsTranslator().translate(input).render(it) }
    }

    private fun shiftColors() {
        val input = File(androidAppDir + "/src/main/res/values/colors.xml")
        val output = File(iosAppDir + "/Values/Colors.swift")
        output.createWithParent()
        FileWriter(output).use { ColorsTranslator().translate(input).render(it) }
    }

    private fun shiftDimens() {
        val input = File(androidAppDir + "/src/main/res/values/dimens.xml")
        val output = File(iosAppDir + "/Values/Dimens.swift")
        output.createWithParent()
        FileWriter(output).use { DimensTranslator().translate(input).render(it) }
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        val layoutDir = File(androidAppDir + "/src/main/res/layout/")
        layoutDir.listFiles { file ->
            if (file.name == "activity_main.xml") {
                val output = File(iosAppDir + "/Layout/${file.name.toIosResourceName()}Layout.swift")
                output.createWithParent()
                FileWriter(output).use { translator.translate(file).render(it) }
            }
            true
        }
    }

    private fun File.createWithParent() {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
    }
}