package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.StringsFile
import org.w3c.dom.Document

class StringsTranslator : Translator<StringsFile>() {

    override fun generateFile(doc: Document): StringsFile {
        val file = StringsFile()
        for (string in doc.getElementsByTagName("string").iterator()) {
            if (string != null) {
                val name = string.attributes.getNamedItem("name").textContent
                val value = string.firstChild.nodeValue
                file.addString(name, value)
            }
        }
        return file
    }
}

