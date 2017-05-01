package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.toCamelCase
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class ColorsTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        return SwiftFile.create {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            import("UIKit")
            struct("Colors") {
                createColorMap(doc.getElementsByTagName("color")).forEach { name, value ->
                    constant(name, value) {
                        declarationModifier(DeclarationModifier.STATIC)
                    }
                }
            }
        }
    }

    private fun createColorMap(colors: NodeList): Map<String, String> {
        val colorMap = mutableMapOf<String, String>()
        for (color in colors.iterator()) {
            if (color != null) {
                val name = color.attributes.getNamedItem("name").textContent.toCamelCase()
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
