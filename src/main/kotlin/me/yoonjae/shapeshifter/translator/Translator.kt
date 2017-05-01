package me.yoonjae.shapeshifter.translator

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File

abstract class Translator<out F : me.yoonjae.shapeshifter.poet.file.File> {

    abstract fun translate(file: File): F

//        FileInputStream(inputFile).use {
//            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
//            return onTranslate(doc, inputFile.name)
//            if (!outputFile.exists()) {
//                val parentFile = outputFile.parentFile
//                if (!parentFile.exists()) parentFile.mkdirs()
//                outputFile.createNewFile()
//            }
//            FileWriter(outputFile).use {
//                swiftFile.render(it)
//            }
//        }
//    }
}

fun NodeList.iterator(): Iterator<Node?> {
    return object : Iterator<Node?> {
        var index: Int = 0

        override fun hasNext() = index < length

        override fun next() = item(index++)
    }
}