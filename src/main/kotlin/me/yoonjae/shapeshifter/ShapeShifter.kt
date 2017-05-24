package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.*
import me.yoonjae.shapeshifter.translator.system.*
import java.io.File
import java.io.FileWriter

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    val system = listOf(theme, textAppearance, textStyle, cgSizeExtension, uiEdgeInsetsExtension,
            uiFontExtension, gravity, view, viewGroup, frameLayout, linearLayout, textView, button,
            imageView)


    fun shift() {
        system.forEach { it.writeTo(iosAppDir + "/System/") }
        shiftStringResource()
        shiftAppThemeResource()
        shiftColorResource()
        shiftDimenResource()
        shiftLayouts()
        shiftControllers()
    }

    private fun shiftStringResource() {
        val input = File(androidAppDir + "/src/main/res/values/strings.xml")
        StringResourceTranslator().translate(input).writeTo(iosAppDir + "/ko.lproj/")
    }

    private fun shiftAppThemeResource() {
        val input = File(androidAppDir + "/src/main/res/values/styles.xml")
        AppThemeResourceTranslator().translate(input).writeTo(iosAppDir + "/Values/")
    }

    private fun shiftColorResource() {
        val input = File(androidAppDir + "/src/main/res/values/colors.xml")
        ColorResourceTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftDimenResource() {
        val input = File(androidAppDir + "/src/main/res/values/dimens.xml")
        DimenResourceTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        File(androidAppDir + "/src/main/res/layout/").listFiles { file ->
            val swiftFile = translator.translate(file)
            val outputFile = File("$iosAppDir/Layouts/${swiftFile.name}")
            val modified = outputFile.exists() && outputFile.readLines().getOrNull(0)?.let {
                it.contains("//") && it.contains("modified")
            } ?: false
            if (!modified) {
                swiftFile.writeTo(iosAppDir + "/Layouts")
            }
            true
        }
    }

    private fun shiftControllers() {
        val translator = ControllerTranslator()
        File(androidAppDir + "/src/main/java/com/soomgo/controller/").listFiles { file ->
            if (file.isDirectory) {
                file.listFiles().forEach {
                    translator.translate(it)
                            .writeTo(iosAppDir + "/Controllers/" + file.name.capitalize(), true)
                }
            } else if (file.name.contains("Activity") || file.name.contains("Fragment")) {
                translator.translate(file).writeTo(iosAppDir + "/Controllers/", true)
            }
            true
        }
    }

    private fun me.yoonjae.shapeshifter.poet.file.File.writeTo(path: String,
                                                               preventOverlap: Boolean = false) {
        val file = File("$path/$name")
        if (!preventOverlap || !file.exists()) {
            FileWriter(file.createWithParent()).use { render(it) }
        }
    }

    private fun File.createWithParent(): File {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
        return this
    }
}