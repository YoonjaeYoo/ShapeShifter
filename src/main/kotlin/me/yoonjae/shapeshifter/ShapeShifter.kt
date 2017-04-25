package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.translator.ColorsTranslator
import me.yoonjae.shapeshifter.translator.DimensTranslator
import me.yoonjae.shapeshifter.translator.LayoutTranslator
import me.yoonjae.shapeshifter.translator.StringsTranslator
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    fun shift() {
//        shiftStrings()
//        shiftColors()
//        shiftDimens()
        shiftLayouts()
    }

    private fun shiftStrings() {
        StringsTranslator().translate(androidAppDir + "/src/main/res/values/strings.xml",
                iosAppDir + "/ko.lproj/Localizable.strings")
    }

    private fun shiftColors() {
        ColorsTranslator().translate(androidAppDir + "/src/main/res/values/colors.xml",
                iosAppDir + "/Values/Colors.swift")
    }

    private fun shiftDimens() {
        DimensTranslator().translate(androidAppDir + "/src/main/res/values/dimens.xml",
                iosAppDir + "/Values/Dimens.swift")
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        val layoutDir = File(androidAppDir + "/src/main/res/layout/")
        val nodes = mutableListOf<String>()
        layoutDir.listFiles { file ->
            //            translator.translate(file.path, iosAppDir + "/Layout/${iosFileName(file)}")
            FileInputStream(file.path).use {
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
                translator.parseChildNodes(doc).forEach { node ->
                    if (!nodes.contains(node)) {
                        nodes.add(node)
                    }
                }
            }
            true
        }
        nodes.sorted().forEach(::println)
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
        return (if (builder.isEmpty()) "" else builder.toString()) +
                (if (prefixBuilder.isEmpty()) "" else prefixBuilder.toString())
    }
}