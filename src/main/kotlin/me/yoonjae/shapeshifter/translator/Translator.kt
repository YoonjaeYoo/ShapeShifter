package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.SwiftFile
import org.w3c.dom.Document
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory

abstract class Translator(val androidAppDir: String, val iosAppDir: String) {

    abstract fun getAndroidFilePath(): String
    abstract fun getIosFilePath(): String

    open fun translate() {
        FileInputStream("$androidAppDir/${getAndroidFilePath()}").use {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
            val swiftFile = generateSwiftFile(doc)
            val file = File("$iosAppDir/${getIosFilePath()}")
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

    abstract fun generateSwiftFile(doc: Document): SwiftFile
}