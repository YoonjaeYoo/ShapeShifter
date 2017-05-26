package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.extensions.attr
import me.yoonjae.shapeshifter.translator.extensions.elements
import me.yoonjae.shapeshifter.translator.extensions.toColor
import me.yoonjae.shapeshifter.translator.system.themeFields
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class AppThemeResourceTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        return SwiftFile("AppTheme.swift") {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            val styles = doc.documentElement.childNodes.elements()
            styles.find { it.getAttribute("name") == "AppTheme" }?.let { style ->
                import("UIKit")
                import("UIColor_Hex_Swift")
                clazz("AppTheme") {
                    public()
                    superType("Theme")

                    initializer {
                        initializerExpression("super") {
                            themeArguments(style)
                        }
                    }

                    styles.filter { it.getAttribute("name").startsWith("AppTheme.") }.forEach {
                        clazz(it.getAttribute("name").substring(9)) {
                            superType("Theme")

                            initializer {
                                initializerExpression("super") {
                                    themeArguments(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ArgumentDescriber.themeArguments(element: Element) {
        themeFields.forEach { name, type ->
            element.childNodes.elements().asSequence().find {
                it.tagName == "item" && it.attr("name")?.endsWith(name)!!
            }?.textContent?.let {
                argument(name, when (type.name) {
                    "UIColor" -> it.toColor()
                    else -> throw RuntimeException()
                })
            }
        }
    }
}
