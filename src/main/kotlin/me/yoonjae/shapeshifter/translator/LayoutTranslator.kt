package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifier
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File

class LayoutTranslator : Translator<SwiftFile>() {

    override fun generateFile(doc: Document, inputFile: File, outputFile: File): SwiftFile {
        return SwiftFile.create {
            import("UIKit")
            import("LayoutKit")
            clazz(parseClassName(outputFile), "StackLayout") {
                genericParameter("T")
                initializer {
                    modifier(DeclarationModifier.CONVENIENCE)
                    parameter("str", "String", "a", "\"STR\"") {
                        inout(true)
                        variadic(true)
                    }
                }
            }
        }
    }

    private fun parseClassName(outputFile: File): String {
        val name = outputFile.name
        return name.substring(0, name.indexOfLast { it == '.' })
    }

    fun parseChildNodes(parent: Node): List<String> {
        val nodes = mutableListOf<String>()
        for (node in parent.childNodes.iterator()) {
            if (node != null && node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                var name = element.nodeName
                nodes.add(name)
                println("  $name")

//                if (node.hasChildNodes()) {
//                    nodes.addAll(parseChildNodes(node))
//                }
            }
        }
        return nodes
    }
}