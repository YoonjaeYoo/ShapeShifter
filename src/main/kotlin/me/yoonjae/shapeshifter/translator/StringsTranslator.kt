package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.StringsFile
import me.yoonjae.shapeshifter.translator.extensions.iterator
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class StringsTranslator : Translator<StringsFile>() {

    override fun translate(file: File): StringsFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return StringsFile.create {
            for (string in doc.getElementsByTagName("string").iterator()) {
                if (string != null) {
                    val name = string.attributes.getNamedItem("name").textContent
                    val value = string.firstChild.nodeValue
                    string(name, value)
                }
            }
        }
    }
}

