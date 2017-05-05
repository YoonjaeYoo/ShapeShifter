package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.expression.ClosureExpression
import me.yoonjae.shapeshifter.poet.expression.Expression
import me.yoonjae.shapeshifter.poet.expression.GeneralExpression
import me.yoonjae.shapeshifter.poet.expression.InitializerExpression
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
            val rootElement = doc.documentElement
            clazz(className, Type("InsetLayout")) {
                initializer {
                    accessLevelModifier = AccessLevelModifier.PUBLIC
                    initializerExpression("super") {
                        translateLayout(rootElement)?.let {
                            argument("sublayout", it)
                        }
                    }
                }
            }
        }
    }

    private fun translateLayout(element: Element): Expression? {
        return when (element.tagName) {
            "FrameLayout" -> translateFrameLayout(element)
            else -> null
        }?.apply {
            val id = element.getAttribute("android:id")
            if (id != null) {
                trailingClosure = ClosureExpression().apply {
                    closureParameter("id", Type("String"))
                    generalExpression("let test = 1")
                }
            }
        }
    }

    private fun translateFrameLayout(element: Element): InitializerExpression? =
            InitializerExpression(GeneralExpression("InsetLayout")).apply {
            }
}