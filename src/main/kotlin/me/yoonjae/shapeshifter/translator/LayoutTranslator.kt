package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.*
import me.yoonjae.shapeshifter.translator.requirements.appTheme
import me.yoonjae.shapeshifter.translator.requirements.buttonLayout
import me.yoonjae.shapeshifter.translator.requirements.pileLayout
import me.yoonjae.shapeshifter.translator.requirements.system
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    fun requirements(): List<SwiftFile> {
        return listOf(system, appTheme, pileLayout, buttonLayout)
    }

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val resourceName = file.name.substring(0, file.name.lastIndexOf('.')).toResourceName()
        val idTypeMap = mutableMapOf<String, String>()
        extractIdTypeMap(idTypeMap, doc.documentElement)
        return SwiftFile("${resourceName}Layout.swift") {
            import("UIKit")
            import("LayoutKit")

            clazz("${resourceName}Layout") {
                superType("InsetLayout") {
                    genericParameter("UIView")
                }
                initializer {
                    public()
                    idTypeMap.forEach { id, type ->
                        parameter(id.toConfigParameterName(), Type("(($type) -> Void)? = nil"))
                    }
                    initializerExpression("super") {
                        argument("insets") {
                            initializerExpression("EdgeInsets")
                        }
                        argument("sublayout") {
                            layoutExpression(doc.documentElement)
                        }
                    }
                }
            }
        }
    }

    private fun extractIdTypeMap(ids: MutableMap<String, String>, element: Element) {
        element.id()?.let {
            ids.put(it, getType(element))
        }
        element.childNodes.elementIterator().forEach { extractIdTypeMap(ids, it) }
    }

    private fun getType(element: Element): String {
        return when (element.tagName) {
            "FrameLayout" -> "UIView"
            "ImageView" -> "UIImageView"
            "Button" -> "UIButton"
            else -> "UIView"
        }
    }
}
