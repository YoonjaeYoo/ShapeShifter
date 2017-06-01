package me.yoonjae.shapeshifter

import me.yoonjae.shapeshifter.system.*
import me.yoonjae.shapeshifter.translator.*
import me.yoonjae.shapeshifter.translator.extensions.attr
import me.yoonjae.shapeshifter.translator.extensions.elements
import org.w3c.dom.Document
import java.io.File
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory

class ShapeShifter(val androidAppDir: String, val iosAppDir: String) {

    val system = listOf(Theme(), TextAppearance(), TextStyle(), CGSizeExtension(),
            CGFloatExtension(), UIEdgeInsetsExtension(), UIFontExtension(), Gravity(), View(),
            ViewGroup(), FrameLayout(), LinearLayout(), TextView(), EditText(), Button(),
            ImageView(), ScrollView())


    fun shift() {
        system.forEach { it.writeTo(iosAppDir + "/System/") }
        shiftStringResource()
        shiftAppThemeResource()
        shiftColorResource()
        shiftDimenResource()
        shiftLayouts()
        shiftControllers()
        shiftModels()
        shiftRestService()
    }

    private fun shiftStringResource() {
        val input = File(androidAppDir + "/src/main/res/values/strings.xml")
        StringResourceTranslator().translate(input).writeTo(iosAppDir + "/ko.lproj/")
    }

    private fun shiftAppThemeResource() {
        val input = File(androidAppDir + "/src/main/res/values/styles.xml")
        AppThemeResourceTranslator().translate(input).writeTo(iosAppDir + "/Values/")
    }

    private fun shiftColorResource() {
        val input = File(androidAppDir + "/src/main/res/values/colors.xml")
        ColorResourceTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftDimenResource() {
        val input = File(androidAppDir + "/src/main/res/values/dimens.xml")
        DimenResourceTranslator().translate(input).writeTo(iosAppDir + "/Values")
    }

    private fun shiftLayouts() {
        val translator = LayoutTranslator()
        File(androidAppDir + "/src/main/res/layout/").listFiles().forEach { file ->
            translator.translate(file)?.writeTo(iosAppDir + "/Layouts")
        }
    }

    private fun shiftControllers() {
        val manifest = File(androidAppDir + "/src/main/AndroidManifest.xml")
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifest)
        val translator = ControllerTranslator(extractActivityThemeMap(doc))
        File(androidAppDir + "/src/main/java/com/soomgo/controller/").listFiles().forEach { file ->
            if (file.isDirectory) {
                file.listFiles().forEach {
                    translator.translate(it)
                            .writeTo(iosAppDir + "/Controllers/" + file.name.capitalize(), true)
                }
            } else if (file.name.contains("Activity") || file.name.contains("Fragment")) {
                translator.translate(file).writeTo(iosAppDir + "/Controllers/", true)
            }
        }
    }

    private fun shiftModels() {
        val translator = ModelTranslator()
        File(androidAppDir + "/src/main/java/com/soomgo/model/").listFiles().forEach { file ->
            translator.translate(file)?.writeTo(iosAppDir + "/Models/")
        }
    }

    private fun shiftRestService() {
        val translator = RestServiceTranslator()
        val file = File(androidAppDir + "/src/main/java/com/soomgo/rest/SoomgoService.java")
        translator.translate(file)?.writeTo(iosAppDir + "/Rest/")
    }

    private fun extractActivityThemeMap(doc: Document): Map<String, String> {
        val map = mutableMapOf<String, String>()
        doc.getElementsByTagName("activity").elements().forEach {
            it.attr("android:name")?.let { name ->
                it.attr("android:theme")?.let {
                    map.put(name.split(".").last(), it.substring(7)) // @style/...
                }
            }
        }
        return map
    }

    private fun me.yoonjae.shapeshifter.poet.file.File.writeTo(path: String,
                                                               preventOverlap: Boolean = false) {
        val file = File("$path/$name")
        if (!preventOverlap || !file.exists()) {
            if (!file.isModified()) {
                FileWriter(file.createWithParent()).use { render(it) }
            }
        }
    }

    private fun File.isModified(): Boolean {
        return exists() && readLines().getOrNull(0)?.let {
            it.contains("//") && it.contains("modified")
        } ?: false
    }

    private fun File.createWithParent(): File {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            createNewFile()
        }
        return this
    }
}