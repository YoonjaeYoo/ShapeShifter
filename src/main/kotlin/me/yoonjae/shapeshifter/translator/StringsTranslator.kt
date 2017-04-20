package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.StringsFile
import org.w3c.dom.Document

class StringsTranslator(androidAppDir: String, iosAppDir: String) :
        Translator<StringsFile>(androidAppDir, iosAppDir) {

    override fun getAndroidFilePath(): String = "/src/main/res/values/strings.xml"
    override fun getIosFilePath(): String = "/ko.lproj/Localizable.strings"

    override fun generateFile(doc: Document): StringsFile {
        val file = StringsFile()
        for (string in doc.getElementsByTagName("string").iterator()) {
            if (string != null) {
                val name = string.attributes.getNamedItem("name").textContent
                var value = string.firstChild.nodeValue
                file.addString(name, value)
            }
        }
        return file
    }
}

