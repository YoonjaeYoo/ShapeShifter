package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.*
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val resourceName = file.name.substring(0, file.name.lastIndexOf('.')).toResourceName(true)
        val idTypeMap = mutableMapOf<String, String>()
        extractIdTypeMap(idTypeMap, doc.documentElement)
        return SwiftFile("${resourceName}Layout.swift") {
            import("UIKit")
            import("LayoutKit")
            import("Localize_Swift")

            clazz("${resourceName}Layout") {
                superType(doc.documentElement.layoutType)
                initializer {
                    public()
                    parameter("theme", Type("Theme"), "AppTheme()")
                    parameter("config", Type("(${doc.documentElement.viewType}) -> Void", true),
                            "nil")
                    idTypeMap.forEach { id, type ->
                        parameter(id.toConfigParameterName(), Type("($type) -> Void", true), "nil")
                    }
                    layoutExpression(doc.documentElement)
                }
            }
        }
    }

    private fun extractIdTypeMap(ids: MutableMap<String, String>, element: Element) {
        element.id()?.let {
            ids.put(it, element.viewType)
        }
        element.childNodes.elements().forEach { extractIdTypeMap(ids, it) }
    }
}
