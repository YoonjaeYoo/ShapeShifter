package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.Field
import me.yoonjae.shapeshifter.poet.Struct
import me.yoonjae.shapeshifter.poet.SwiftFile
import me.yoonjae.shapeshifter.poet.toCamelCase
import org.w3c.dom.Document
import org.w3c.dom.NodeList

class DimensTranslator(androidAppDir: String, iosAppDir: String) :
        Translator<SwiftFile>(androidAppDir, iosAppDir) {

    override fun getAndroidFilePath() = "/src/main/res/values/dimens.xml"

    override fun getIosFilePath() = "/Values/Dimens.swift"

    override fun generateFile(doc: Document): SwiftFile {
        val file = SwiftFile()
        val struct = Struct("Dimens")
        createDimenMap(doc.getElementsByTagName("dimen")).forEach { name, value ->
            struct.addField(Field(name).value(value).static(true).let(true))
        }
        file.addStruct(struct)
        return file
    }

    private fun createDimenMap(dimens: NodeList): Map<String, String> {
        val dimenMap = mutableMapOf<String, String>()
        for (dimen in dimens.iterator()) {
            if (dimen != null) {
                val name = dimen.attributes.getNamedItem("name").textContent.toCamelCase()
                var value = dimen.firstChild.nodeValue
                if (value.endsWith("dp")) {
                    value = value.substring(0, value.length - 2)
                    dimenMap.put(name, value)
                }
            }
        }
        return dimenMap
    }
}