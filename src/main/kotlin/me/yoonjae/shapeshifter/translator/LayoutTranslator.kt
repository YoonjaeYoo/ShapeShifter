package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.translator.extensions.layoutExpression
import me.yoonjae.shapeshifter.translator.extensions.toIosResourceName
import me.yoonjae.shapeshifter.translator.requirements.pileLayout
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    fun requirements(): List<SwiftFile> {
        return listOf(pileLayout)
    }

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val resourceName = file.name.substring(0, file.name.lastIndexOf('.')).toIosResourceName()
        return SwiftFile("${resourceName}Layout.swift") {
            import("UIKit")
            import("LayoutKit")

            clazz("${resourceName}Layout") {
                superType("InsetLayout") {
                    genericParameter("UIView")
                }
                initializer {
                    public()
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
}
