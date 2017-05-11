package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.extensions.iterator
import me.yoonjae.shapeshifter.translator.extensions.toCamelCase
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class ColorsTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        return SwiftFile("Color.swift") {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            import("UIKit")
            import("UIColor_Hex_Swift")
            struct("Color") {
                createColorMap(doc.getElementsByTagName("color")).forEach { name, value ->
                    constant(name, value = value) {
                        static()
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
