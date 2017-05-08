package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.ColorsTranslator
import me.yoonjae.shapeshifter.translator.DimensTranslator
import me.yoonjae.shapeshifter.translator.LayoutTranslator
import me.yoonjae.shapeshifter.translator.StringsTranslator
import me.yoonjae.shapeshifter.translator.extensions.elementIterator
import org.w3c.dom.Element
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
        val frameLayoutChildren = mutableListOf<String>()

        translator.requirements().forEach {
            it.writeTo(iosAppDir + "/Layout/")
        }
        layoutDir.listFiles { file ->
            if (file.name == "activity_test.xml") {
                val path = iosAppDir + "/Layout/"
                translator.translate(file).writeTo(path)
            }

//            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
//            getFrameLayoutChildren(doc.documentElement, frameLayoutChildren)

            true
        }
        frameLayoutChildren.forEach { println(it) }
    }

    private fun SwiftFile.writeTo(path: String) {
        val output = File("$path/$name")
        output.createWithParent()
        FileWriter(output).use { render(it) }
    }

    private fun getFrameLayoutChildren(element: Element, frameLayoutChildren: MutableList<String>) {
        for (child in element.childNodes.elementIterator()) {
            if (element.tagName == "FrameLayout") {
                val width = child.getAttribute("android:layout_width")
                val height = child.getAttribute("android:layout_height")
                val info = "${child.tagName}($width, $height)"
                if (!frameLayoutChildren.contains(info)) {
                    frameLayoutChildren.add(info)
                }
            }
            getFrameLayoutChildren(child, frameLayoutChildren)
        }
    }

    private fun File.createWithParent() {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
    }
}