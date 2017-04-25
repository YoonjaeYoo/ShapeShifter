package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.SwiftFile
import org.w3c.dom.Document
import org.w3c.dom.Node

class LayoutTranslator : Translator<SwiftFile>() {

    override fun generateFile(doc: Document): SwiftFile {
        val file = SwiftFile()
        return file
    }

    fun parseChildNodes(parent: Node): List<String> {
        val nodes = mutableListOf<String>()
        for (node in parent.childNodes.iterator()) {
            if (node != null && node.nodeType == Node.ELEMENT_NODE) {
                nodes.add(node.nodeName)
            }
        }
        return nodes
    }
}