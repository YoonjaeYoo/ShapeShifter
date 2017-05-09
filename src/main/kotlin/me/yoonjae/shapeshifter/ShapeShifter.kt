package me.yoonjae.shapeshifter

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
        StringsTranslator().translate(input).writeTo(iosAppDir + "/ko.lproj/")
    }

    private fun shiftColors() {
        val input = File(androidAppDir + "/src/main/res/values/colors.xml")
        ColorsTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftDimens() {
        val input = File(androidAppDir + "/src/main/res/values/dimens.xml")
        DimensTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        val layoutDir = File(androidAppDir + "/src/main/res/layout/")
        val frameLayoutChildren = mutableListOf<String>()

        translator.requirements().forEach {
            it.writeTo(iosAppDir + "/Layout/")
        }
        layoutDir.listFiles { file ->
            if (file.name == "activity_test.xml") {
                translator.translate(file).writeTo(iosAppDir + "/Layout/")
            }
            true
        }
        frameLayoutChildren.forEach { println(it) }
    }

    private fun me.yoonjae.shapeshifter.poet.file.File.writeTo(path: String) {
        val output = File("$path/$name")
        output.createWithParent()
        FileWriter(output).use { render(it) }
    }

    private fun File.createWithParent() {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
    }
}