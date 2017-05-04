package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.declaration.Class
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.toIosResourceName
import me.yoonjae.shapeshifter.poet.type.Type
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return SwiftFile.create {
            import("UIKit")
            import("LayoutKit")

            val className = file.name.substring(0, file.name.lastIndexOf('.')).toIosResourceName()
            clazz(className, Type("InsetLayout")) {
                initializer(AccessLevelModifier.PUBLIC) {
                    initializerExpression("super") {
                        argument("insets", "0")
                        argument("alignment", "Alignment.FILL")
                    }
                }
                val rootElement = doc.documentElement
                when (rootElement.tagName) {
                    "FrameLayout" -> translateFrameLayout(this, rootElement)
                }
            }
        }
    }

    private fun translateFrameLayout(clazz: Class, rootElement: Element) {

    }
}