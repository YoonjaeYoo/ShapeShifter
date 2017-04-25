package me.yoonjae.shapeshifter.translator

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory

abstract class Translator<out F : me.yoonjae.shapeshifter.poet.File> {

    open fun translate(inputPath: String, outputPath: String) {
        FileInputStream(inputPath).use {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
            val swiftFile = generateFile(doc)
            val file = File(outputPath)
            if (!file.exists()) {
                val parentFile = file.parentFile
                if (!parentFile.exists()) parentFile.mkdirs()
                file.createNewFile()
            }
            FileWriter(file).use {
                swiftFile.writeTo(it)
            }
        }
    }

    abstract fun generateFile(doc: Document): F
}

fun NodeList.iterator(): Iterator<Node?> {
    return object : Iterator<Node?> {
        var index: Int = 0

        override fun hasNext() = index < length

        override fun next() = item(index++)
    }
}