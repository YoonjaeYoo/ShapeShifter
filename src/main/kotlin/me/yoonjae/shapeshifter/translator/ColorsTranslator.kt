package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.Field
import me.yoonjae.shapeshifter.poet.Import
import me.yoonjae.shapeshifter.poet.Struct
import me.yoonjae.shapeshifter.poet.SwiftFile
import org.w3c.dom.Document
import org.w3c.dom.NodeList

class ColorsTranslator(androidAppDir: String, iosAppDir: String) : Translator(androidAppDir, iosAppDir) {

    override fun getAndroidFilePath(): String = "/src/main/res/values/colors.xml"

    override fun getIosFilePath(): String = "/Values/Colors.swift"

    override fun generateSwiftFile(doc: Document): SwiftFile {
        val file = SwiftFile().addImport(Import("UIKit"))
        val struct = Struct("Color")
        createColorMap(doc.getElementsByTagName("color")).forEach { name, value ->
            struct.addField(Field(name).value(value).static(true).let(true))
        }
        file.addStruct(struct)
        return file
    }

    private fun createColorMap(colors: NodeList): Map<String, String> {
        val colorMap = mutableMapOf<String, String>()
        for (i in 0..colors.length) {
            val color = colors.item(i)
            if (color != null) {
                val name = color.attributes.getNamedItem("name").textContent
                var value = color.firstChild.nodeValue
                if (value.startsWith("@color/")) {
                    value = value.substring(7)
                } else {
                    value = "UIColor(\"$value\")"
                }
                colorMap.put(name, value)
            }
        }
        return colorMap
    }
}

class Color(val alpha: Float, val red: Float, val green: Float, val blue: Float)

fun String.toColor(): Color? {
    var hex = this
    if (startsWith("#")) {
        hex = hex.substring(1)
    }
    if (hex.toIntOrNull(16) == null) {
        return null
    }
    val length = hex.length
    if (length <= 4) {
        return Color(if (length == 4) hex[length - 4].toString().repeat(2).toColorValue() else 1F,
                hex[length - 1].toString().repeat(2).toColorValue(),
                hex[length - 2].toString().repeat(2).toColorValue(),
                hex[length - 3].toString().repeat(2).toColorValue())
    } else {
        return Color(if (length == 4) hex.substring(length - 8, length - 6).toColorValue() else 1F,
                hex.substring(length - 2, length).toColorValue(),
                hex.substring(length - 4, length - 2).toColorValue(),
                hex.substring(length - 6, length - 4).toColorValue())
    }
}

private fun String.toColorValue(): Float {
    return toInt(16) * 1F / 256
}
