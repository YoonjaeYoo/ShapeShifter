package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.StringsFile
import me.yoonjae.shapeshifter.translator.extensions.iterator
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class StringResourceTranslator : Translator<StringsFile>() {

    override fun translate(file: File): StringsFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return StringsFile {
            for (string in doc.getElementsByTagName("string").iterator()) {
                if (string != null) {
                    val name = string.attributes.getNamedItem("name").textContent
                    var value = string.firstChild.nodeValue
                    mapOf(Regex("%\\d\\$[s]") to "%@", Regex("%\\d\\$") to "%").forEach {
                        if (value.contains(it.key)) {
                            value = value.replace(it.key, it.value)
                        }
                    }
                    string(name, value)
                }
            }
        }
    }
}

