package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.extensions.iterator
import me.yoonjae.shapeshifter.translator.extensions.toCamelCase
import me.yoonjae.shapeshifter.translator.extensions.toColor
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class ColorsTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        return SwiftFile("Color.swift") {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            import("UIKit")
            import("UIColor_Hex_Swift")
            struct("Color") {
                doc.getElementsByTagName("color").iterator().forEach {
                    if (it != null) {
                        val name = it.attributes.getNamedItem("name").textContent.toCamelCase()
                        val value = it.firstChild.nodeValue.toColor()
                        constant(name, value = value) {
                            static()
                        }
                    }
                }
            }
        }
    }
}
