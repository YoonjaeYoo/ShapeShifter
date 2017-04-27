package me.yoonjae.shapeshifter.translator

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory

abstract class Translator<out F : me.yoonjae.shapeshifter.poet.file.File> {

    open fun translate(inputFile: File, outputFile: File) {
        FileInputStream(inputFile).use {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
            val swiftFile = generateFile(doc, inputFile, outputFile)
            if (!outputFile.exists()) {
                val parentFile = outputFile.parentFile
                if (!parentFile.exists()) parentFile.mkdirs()
                outputFile.createNewFile()
            }
            FileWriter(outputFile).use {
                swiftFile.render(it)
            }
        }
    }

    abstract fun generateFile(doc: Document, inputFile: File, outputFile: File): F
}

fun NodeList.iterator(): Iterator<Node?> {
    return object : Iterator<Node?> {
        var index: Int = 0

        override fun hasNext() = index < length

        override fun next() = item(index++)
    }
}