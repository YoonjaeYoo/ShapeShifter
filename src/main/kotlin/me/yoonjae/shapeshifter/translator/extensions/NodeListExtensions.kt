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

fun NodeList.elementIterator(): Iterator<Element> {
    return object : Iterator<Element> {
        private var index = 0

        override fun hasNext(): Boolean {
            (index until length).forEach {
                if (item(it).isElement()) return true
            }
            return false
        }

        override fun next(): Element {
            (index until length).forEach {
                val item = item(it)
                if (item.isElement()) {
                    index = it + 1
                    return item as Element
                }
            }
            throw NoSuchElementException()
        }

        private fun Node?.isElement() = this?.nodeType == Node.ELEMENT_NODE
    }
}

