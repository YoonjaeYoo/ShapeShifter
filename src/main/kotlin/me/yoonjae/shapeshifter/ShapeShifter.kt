package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.*
import me.yoonjae.shapeshifter.translator.system.*
import java.io.File
import java.io.FileWriter

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    val system = listOf(drawable, theme, textAppearance, textStyle, cgSizeExtension,
            uiFontExtension, gravity, view, viewGroup, frameLayout, textView, button)


    fun shift() {
        system.forEach { it.writeTo(iosAppDir + "/System/") }
        shiftStrings()
        shiftAppThemes()
        shiftColors()
        shiftDimens()
        shiftLayouts()
    }

    private fun shiftStrings() {
        val input = File(androidAppDir + "/src/main/res/values/strings.xml")
        StringsTranslator().translate(input).writeTo(iosAppDir + "/ko.lproj/")
    }

    private fun shiftAppThemes() {
        val input = File(androidAppDir + "/src/main/res/values/styles.xml")
        AppThemesTranslator().translate(input).writeTo(iosAppDir + "/Values/")
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
        File(androidAppDir + "/src/main/res/layout/").listFiles { file ->
            println(file.name)
            if (file.name == "activity_test.xml") {
                translator.translate(file).writeTo(iosAppDir + "/Layout/")
            }
            true
        }
    }

    private fun me.yoonjae.shapeshifter.poet.file.File.writeTo(path: String) {
        FileWriter(File("$path/$name").createWithParent()).use { render(it) }
    }

    private fun File.createWithParent(): File {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
        return this
    }
}