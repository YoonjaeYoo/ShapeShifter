package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.ColorsTranslator
import me.yoonjae.shapeshifter.translator.DimensTranslator
import me.yoonjae.shapeshifter.translator.LayoutTranslator
import me.yoonjae.shapeshifter.translator.StringsTranslator
import java.io.File

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    fun shift() {
//        shiftStrings()
//        shiftColors()
//        shiftDimens()
        shiftLayouts()
    }

    private fun shiftStrings() {
        StringsTranslator().translate(File(androidAppDir + "/src/main/res/values/strings.xml"),
                File(iosAppDir + "/ko.lproj/Localizable.strings"))
    }

    private fun shiftColors() {
        ColorsTranslator().translate(File(androidAppDir + "/src/main/res/values/colors.xml"),
                File(iosAppDir + "/Values/Colors.swift"))
    }

    private fun shiftDimens() {
        DimensTranslator().translate(File(androidAppDir + "/src/main/res/values/dimens.xml"),
                File(iosAppDir + "/Values/Dimens.swift"))
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        val layoutDir = File(androidAppDir + "/src/main/res/layout/")
        val nodes = mutableListOf<String>()
        layoutDir.listFiles { file ->
            //            FileInputStream(file.path).use {
//                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
//                translator.parseChildNodes(doc).forEach { node ->
//                    if (!nodes.contains(node)) {
//                        nodes.add(node)
//                    }
//                }
//            }
            if (file.name == "activity_main.xml") {
                translator.translate(file, File(iosAppDir + "/Layout/${iosFileName(file)}.swift"))
            }
            true
        }
    }

    private fun iosFileName(androidFile: File): String {
        val name = androidFile.name
        val prefixBuilder = StringBuilder()
        val builder = StringBuilder()
        var prefix = true
        var initial = true
        for (c in name.toCharArray()) {
            if (c == '_') {
                if (prefix) prefix = false
                initial = true
            } else if (c == '.') {
                break
            } else {
                val b = if (prefix) prefixBuilder else builder
                b.append(if (initial) c.toUpperCase() else c)
                initial = false
            }
        }
        return ((if (builder.isEmpty()) "" else builder.toString()) +
                (if (prefixBuilder.isEmpty()) "" else prefixBuilder.toString())) + "Layout"
    }
}