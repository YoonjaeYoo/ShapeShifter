package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return SwiftFile.create {
            import("UIKit")
            import("LayoutKit")
        }
    }
}