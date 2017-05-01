package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import me.yoonjae.shapeshifter.poet.toCamelCase
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class DimensTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return SwiftFile.create {
            struct("Dimens") {
                createDimenMap(doc.getElementsByTagName("dimen")).forEach { name, value ->
                    constant(name, value = value) {
                        declarationModifier(DeclarationModifier.STATIC)
                    }
                }
            }
        }
    }

    private fun createDimenMap(dimens: NodeList): Map<String, String> {
        val dimenMap = mutableMapOf<String, String>()
        for (dimen in dimens.iterator()) {
            if (dimen != null) {
                val name = dimen.attributes.getNamedItem("name").textContent.toCamelCase()
                var value = dimen.firstChild.nodeValue
                if (value.endsWith("dp")) {
                    value = value.substring(0, value.length - 2)
                    dimenMap.put(name, value)
                }
            }
        }
        return dimenMap
    }
}