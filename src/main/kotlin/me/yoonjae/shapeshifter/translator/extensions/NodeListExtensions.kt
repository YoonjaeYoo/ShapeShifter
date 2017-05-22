package me.yoonjae.shapeshifter.translator.extensions

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

fun NodeList.iterator(): Iterator<Node?> {
    return object : Iterator<Node?> {
        var index: Int = 0

        override fun hasNext() = index < length

        override fun next() = item(index++)
    }
}

fun NodeList.elements(): List<Element> {
    return (0 until length)
            .map { item(it) }
            .filter { it.isElement() }
            .map { it as Element }
            .toList()
}

private fun Node?.isElement() = this?.nodeType == Node.ELEMENT_NODE

