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
            val idElementMap = mutableMapOf<String, Element>()
            extractIdElementMap(idElementMap, doc.documentElement)
            return SwiftFile("${resourceName}Layout.swift") {
                import("UIKit")
                import("LayoutKit")
                import("Localize_Swift")

                clazz("${resourceName}Layout") {
                    superType(layoutType)

                    idElementMap.forEach { id, element ->
                        element.layoutType?.let {
                            function(id.toResourceName(), Type(it)) {
                                returnStatement {
                                    generalExpression("findView(by: ${id.quoted()}) as! $it")
                                }
                            }
                        }
                    }

                    initializer {
                        public()
                        parameter("theme", Type("Theme"), "AppTheme()")
                        parameter("id", Type("String", true),
                                doc.documentElement.id()?.quoted() ?: "nil")
                        parameter("config",
                                Type("(${resourceName}Layout) -> Void", true), "nil")
                        layoutExpression(doc.documentElement)
                    }
                }
            }
        }
    }

    private fun extractIdElementMap(map: MutableMap<String, Element>, element: Element) {
        element.layoutType?.let {
            element.id()?.let {
                map.put(it, element)
            }
        }
        element.childNodes.elements().forEach { extractIdElementMap(map, it) }
    }

    private fun ExpressionDescriber.layoutExpression(element: Element,
                                                     parent: Element? = null) {
        if (parent == null) {
            initializerExpression("super") {
                layoutArguments(element, parent)
            }
            assignmentExpression("self.config") {
                closureExpression {
                    closureParameter("view")

                    functionCallExpression("config?") {
                        argument(value = "self")
                    }
                }
            }
        } else {
            if (element.tagName == "include") {
                element.attr("layout")?.let {
                    // @layout/progress_bar_circular -> ProgressBarCircularLayout
                    initializerExpression("${it.substring(8).toResourceName(true)}Layout") {
                        layoutArguments(element, parent)
                    }
                }
            } else {
                element.style().let { style ->
                    element.layoutType?.let {
                        if (style != null && style.startsWith("$it.")) {
                            @Suppress("IMPLICIT_CAST_TO_ANY")
                            functionCallExpression(style) {
                                layoutArguments(element, parent)
                            }
                        } else {
                            @Suppress("IMPLICIT_CAST_TO_ANY")
                            initializerExpression(it) {
                                layoutArguments(element, parent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ArgumentDescriber.layoutArguments(element: Element, parent: Element? = null) {
        argument("theme", "theme")
        when (element.layoutType) {
            "FrameLayout" ->
                frameLayoutArguments(element, parent)
            "LinearLayout" -> linearLayoutArguments(element, parent)
            "View<UIView>" -> viewArguments(element, parent)
            "Button" -> buttonArguments(element, parent)
            "TextView" -> textViewArguments(element, parent)
            "EditText" -> editTextArguments(element, parent)
            "ImageView" -> imageViewArguments(element, parent)
            "CircularImageView" -> imageViewArguments(element, parent)
            "ScrollView" -> scrollViewArguments(element, parent)
            "RecyclerView" -> recyclerViewArguments(element, parent)
            else -> {
                idArgument(element, parent)
                if (element.tagName != "include") {
                    layoutParamsArgument(element, parent)
                }
                println("  ${element.tagName}")
            }
        }
    }

    private fun ArgumentDescriber.frameLayoutArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("children") {
            arrayLiteralExpression {
                element.childNodes.elements().forEach {
                    layoutExpression(it, element)
                }
            }
        }
    }

    private fun ArgumentDescriber.linearLayoutArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("orientation") {
            generalExpression(when (element.attr("android:orientation")) {
                "vertical" -> ".vertical"
                else -> ".horizontal"
            })
        }
        element.attr("android:weightSum")?.let {
            argument("weightSum", it)
        }
        argument("children") {
            arrayLiteralExpression {
                element.childNodes.elements().forEach {
                    layoutExpression(it, element)
                }
            }
        }
    }

    private fun ArgumentDescriber.viewArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
    }

    private fun ArgumentDescriber.buttonArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
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

    private fun ArgumentDescriber.textViewArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
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

    private fun ArgumentDescriber.editTextArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
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

    private fun ArgumentDescriber.imageViewArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
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

    private fun ArgumentDescriber.scrollViewArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
        layoutParamsArgument(element, parent)
        paddingArgument(element)
        minWidthArgument(element)
        minHeightArgument(element)
        alphaArgument(element)
        backgroundArgument(element)
        argument("child") {
            element.childNodes.elements().firstOrNull()?.let {
                layoutExpression(it, element)
            }
        }
    }

    private fun ArgumentDescriber.recyclerViewArguments(element: Element, parent: Element? = null) {
        idArgument(element, parent)
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

    private fun ArgumentDescriber.idArgument(element: Element, parent: Element? = null) {
        if (parent == null) {
            argument("id", "id")
        } else {
            element.id()?.let { argument("id", "\"$it\"") }
        }
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
