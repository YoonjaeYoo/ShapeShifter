package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.expression.ArgumentDescriber
import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.*
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile? {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        return doc.documentElement.layoutType?.let { layoutType ->
            val resourceName = file.name.substring(0, file.name.lastIndexOf('.')).toResourceName(true)
            val map = mutableMapOf<String, String>()
            extractIdTypeMap(file.parent, map, doc.documentElement)
            return SwiftFile("${resourceName}Layout.swift") {
                import("UIKit")
                import("LayoutKit")
                import("Localize_Swift")

                clazz("${resourceName}Layout") {
                    superType(layoutType)
                    initializer {
                        public()
                        parameter("theme", Type("Theme"), "AppTheme()")
                        map.forEach { id, type ->
                            parameter(id.toConfigParameterName(), Type("($type) -> Void", true), "nil")
                        }
                        parameter("config", Type("(${doc.documentElement.viewType}) -> Void", true),
                                "nil")
                        layoutExpression(file.parent, doc.documentElement)
                    }
                }
            }
        }
    }

    private fun extractIdTypeMap(path: String, map: MutableMap<String, String>, element: Element) {
        element.layoutType?.let {
            element.id()?.let {
                map.put(it, element.viewType)
            }
        }
        element.childNodes.elements().forEach { extractIdTypeMap(path, map, it) }
        if (element.tagName == "include") {
            element.attr("layout")?.let {
                // @layout/progress_bar_circular -> ProgressBarCircularLayout
                val file = File(path, "${it.substring(8)}.xml")
                val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                extractIdTypeMap(path, map, doc.documentElement)
            }
        }
    }

    private fun ExpressionDescriber.layoutExpression(path: String, element: Element,
                                                     parent: Element? = null) {
        if (parent == null) {
            initializerExpression("super") {
                layoutArguments(path, element, parent)
            }
        } else {
            if (element.tagName == "include") {
                element.attr("layout")?.let {
                    // @layout/progress_bar_circular -> ProgressBarCircularLayout
                    val file = File(path, "${it.substring(8)}.xml")
                    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                    val map = mutableMapOf<String, String>()
                    extractIdTypeMap(path, map, doc.documentElement)
                    initializerExpression("${it.substring(8).toResourceName(true)}Layout") {
                        layoutArguments(path, element, parent, map)
                    }
                }
            } else {
                element.style().let { style ->
                    element.layoutType?.let {
                        if (style != null && style.startsWith("$it.")) {
                            @Suppress("IMPLICIT_CAST_TO_ANY")
                            functionCallExpression(style) {
                                layoutArguments(path, element, parent)
                            }
                        } else {
                            @Suppress("IMPLICIT_CAST_TO_ANY")
                            initializerExpression(it) {
                                layoutArguments(path, element, parent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ArgumentDescriber.layoutArguments(path: String, element: Element, parent: Element? = null,
                                                  idTypeMap: Map<String, String>? = null) {
        argument("theme", "theme")
        when (element.layoutType) {
            "FrameLayout" ->
                frameLayoutArguments(path, element, parent)
            "LinearLayout" -> linearLayoutArguments(path, element, parent)
            "View" -> viewArguments(path, element, parent)
            "Button" -> buttonArguments(path, element, parent)
            "TextView" -> textViewArguments(path, element, parent)
            "EditText" -> editTextArguments(path, element, parent)
            "ImageView" -> imageViewArguments(path, element, parent)
            "ScrollView" -> scrollViewArguments(path, element, parent)
            "RecyclerView" -> recyclerViewArguments(path, element, parent)
            "include" -> {
            }
            else -> {
                idArgument(element)
                layoutParamsArgument(element, parent)
                println("  ${element.tagName}")
            }
        }
        idTypeMap?.forEach { id, _ ->
            val resouceName = id.toResourceName(true)
            argument("config$resouceName", "config$resouceName")
        }
        if (parent == null) {
            argument("config") {
                closureExpression {
                    closureParameter("view")

                    element.id()?.let {
                        functionCallExpression("${it.toConfigParameterName()}?") {
                            argument(value = "view")
                        }
                    }
                    functionCallExpression("config?") {
                        argument(value = "view")
                    }
                }
            }
        } else {
            element.id()?.let {
                argument("config", it.toConfigParameterName())
            }
        }
    }

    private fun String.toConfigParameterName() = "config${toResourceName(true)}"

    private fun ArgumentDescriber.frameLayoutArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("children") {
            arrayLiteralExpression {
                element.childNodes.elements().forEach {
                    layoutExpression(path, it, element)
                }
            }
        }
    }

    private fun ArgumentDescriber.linearLayoutArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        argument("orientation") {
            generalExpression(when (element.attr("android:orientation")) {
                "vertical" -> ".vertical"
                else -> ".horizontal"
            })
        }
        element.attr("android:weightSum")?.let {
            argument("weightSum", it)
        }
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("children") {
            arrayLiteralExpression {
                element.childNodes.elements().forEach {
                    layoutExpression(path, it, element)
                }
            }
        }
    }

    private fun ArgumentDescriber.viewArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
    }

    private fun ArgumentDescriber.buttonArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        gravityArgument(element)
        textArgument(element)
        textAppearanceArgument(element)
        textColorArgument(element)
        textSizeArgument(element)
        textStyleArgument(element)
    }

    private fun ArgumentDescriber.textViewArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        gravityArgument(element)
        listOf("android:lines", "android:singleLine").forEach { name ->
            element.attr(name)?.let {
                argument(name.substring(8), it)
            }
        }
        textArgument(element)
        textAppearanceArgument(element)
        textColorArgument(element)
        textSizeArgument(element)
        textStyleArgument(element)
    }

    private fun ArgumentDescriber.editTextArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        gravityArgument(element)
        element.attr("android:hint")?.let {
            argument("hint", it.parseXmlString())
        }
        textArgument(element)
        textAppearanceArgument(element)
        textColorArgument(element)
        textSizeArgument(element)
        textStyleArgument(element)
    }

    private fun ArgumentDescriber.imageViewArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        element.attr("android:scaleType")?.let {
            argument("scaleType", ".$it")
        }
        element.attr("android:src")?.let {
            argument("src", it.parseXmlImage())
        }
    }

    private fun ArgumentDescriber.scrollViewArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("child") {
            element.childNodes.elements().firstOrNull()?.let {
                layoutExpression(path, it, element)
            }
        }
    }

    private fun ArgumentDescriber.recyclerViewArguments(path: String, element: Element, parent: Element? = null) {
        idArgument(element)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
    }

    private fun ArgumentDescriber.layoutParamsArgument(element: Element, parent: Element? = null,
                                                       init: (ArgumentDescriber.() -> Unit)? = null) {
        argument("layoutParams") {
            initializerExpression(parent.layoutParamsType) {
                argument("width", element.layoutWidth())
                argument("height", element.layoutHeight())
                element.layoutMargin()?.let {
                    argument("margin") {
                        initializerExpression("UIEdgeInsets") {
                            it.forEach { k, v -> argument(k, v) }
                        }
                    }
                }
                element.attr("android:layout_gravity")?.let {
                    argument("gravity", it.parseXmlGravity())
                }
                element.attr("android:layout_weight")?.let {
                    argument("weight", it)
                }
                init?.invoke(this)
            }
        }
    }

    private fun ArgumentDescriber.idArgument(element: Element) {
        element.id()?.let { argument("id", "\"$it\"") }
    }

    private fun ArgumentDescriber.paddingArgument(element: Element) {
        element.padding()?.let {
            argument("padding") {
                initializerExpression("UIEdgeInsets") {
                    it.forEach { k, v -> argument(k, v) }
                }
            }
        }
    }

    private fun ArgumentDescriber.minWidthArgument(element: Element) {
        element.attr("android:minWidth")?.let {
            argument("minWidth", it.parseXmlDimen())
        }
    }

    private fun ArgumentDescriber.minHeightArgument(element: Element) {
        element.attr("android:minHeight")?.let {
            argument("minHeight", it.parseXmlDimen())
        }
    }

    private fun ArgumentDescriber.alphaArgument(element: Element) {
        element.attr("android:alpha")?.let {
            argument("alpha", it)
        }
    }

    private fun ArgumentDescriber.backgroundArgument(element: Element) {
        element.attr("android:background")?.let {
            argument("background", it.parseXmlColor())
        }
    }

    private fun ArgumentDescriber.gravityArgument(element: Element) {
        element.attr("android:gravity")?.let {
            argument("gravity", it.parseXmlGravity())
        }
    }

    private fun ArgumentDescriber.textArgument(element: Element) {
        element.attr("android:text")?.let {
            argument("text", it.parseXmlString())
        }
    }

    private fun ArgumentDescriber.textAppearanceArgument(element: Element) {
        element.attr("android:textAppearance")?.let {
            if (it.startsWith("@style")) {
                argument("textAppearance", "${it.substring(7)}(theme)")
            } else {
                null
            }
        }
    }

    private fun ArgumentDescriber.textColorArgument(element: Element) {
        element.attr("android:textColor")?.let {
            argument("textColor", it.parseXmlColor())
        }
    }

    private fun ArgumentDescriber.textSizeArgument(element: Element) {
        element.attr("android:textSize")?.let {
            argument("textSize", it.parseXmlDimen())
        }
    }

    private fun ArgumentDescriber.textStyleArgument(element: Element) {
        element.attr("android:textStyle")?.let {
            it.split("|").let {
                if (it.isNotEmpty()) {
                    argument("textStyle", ".${it[0]}")
                }
            }
        }
    }
}
