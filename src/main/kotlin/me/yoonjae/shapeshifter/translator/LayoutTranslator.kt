package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.expression.ExpressionDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.alignment
import me.yoonjae.shapeshifter.translator.extensions.elementIterator
import me.yoonjae.shapeshifter.translator.extensions.insets
import me.yoonjae.shapeshifter.translator.extensions.toIosResourceName
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class LayoutTranslator : Translator<SwiftFile>() {

    fun requirements(): List<SwiftFile> {
        return listOf(createPileLayout())
    }

    private fun createPileLayout(): SwiftFile {
        return SwiftFile("PileLayout.swift") {
            import("UIKit")
            import("LayoutKit")

            clazz("PileLayout") {
                open()
                genericParameter("V") { superType("View") }
                superType("BaseLayout") { genericParameter("V") }
                superType("ConfigurableLayout")

                constant("sublayouts", type = Type("[Layout]")) { open() }

                initializer {
                    public()
                    parameter("alignment", "Alignment") { generalExpression(".fill") }
                    parameter("viewReuseId", "String?") { generalExpression("nil") }
                    parameter("sublayouts", "[Layout]")
                    parameter("config", "((V) -> Void)?") { generalExpression("nil") }

                    generalExpression("self.sublayouts = sublayouts")
                    initializerExpression("super") {
                        argument("alignment") { generalExpression("alignment") }
                        argument("flexibility") { generalExpression(".flexible") }
                        argument("viewReuseId") { generalExpression("viewReuseId") }
                        argument("config") { generalExpression("config") }
                    }
                }

                function("measurement", Type("LayoutMeasurement")) {
                    open()
                    parameter("maxSize", "CGSize", "within")

                    constant("size", "maxSize")
                    generalExpression("return LayoutMeasurement(layout: self, size: size, maxSize: maxSize, sublayouts: sublayouts.map { sublayout in\n" +
                            "sublayout.measurement(within: size)\n" +
                            "})")
                }

                function("arrangement", Type("LayoutArrangement")) {
                    open()
                    parameter("rect", "CGRect", "within")
                    parameter("measurement", "LayoutMeasurement")

                    constant("frame", "alignment.position(size: measurement.size, in: rect)")
                    constant("sublayoutRect", "CGRect(x: 0, y: 0, width: frame.width, height: frame.height)")
                    constant("sublayouts", "measurement.sublayouts.map { (measurement) in\n" +
                            "return measurement.arrangement(within: sublayoutRect)\n" +
                            "}")
                    generalExpression("return LayoutArrangement(layout: self, frame: frame, sublayouts: sublayouts)")
                }
            }
        }
    }

    override fun translate(file: File): SwiftFile {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val resourceName = file.name.substring(0, file.name.lastIndexOf('.')).toIosResourceName()
        return SwiftFile("$resourceName.swift") {
            import("UIKit")
            import("LayoutKit")

            clazz("${resourceName}Layout") {
                superType("InsetLayout") {
                    genericParameter("UIView")
                }
                initializer {
                    public()
                    initializerExpression("super") {
                        argument("insets") {
                            initializerExpression("EdgeInsets")
                        }
                        argument("sublayout") {
                            layout(doc.documentElement)
                        }
                    }
                }
            }
        }
    }

    private fun ExpressionDescriber.layout(element: Element, sublayout: Element? = null) {
        when (element.tagName) {
            "FrameLayout" -> frameLayout(element, sublayout)
            else -> generalExpression("nil")
        }
    }

    private fun ExpressionDescriber.frameLayout(element: Element, sublayout: Element? = null) {
        initializerExpression("InsetLayout") {
            argument("insets") {
                initializerExpression("EdgeInsets") {
                    element.insets().forEach { k, v ->
                        argument(k) { generalExpression(v) }
                    }
                }
            }
            argument("alignment") {
                initializerExpression("Alignment") {
                    element.alignment().forEach { k, v ->
                        argument(k) { generalExpression(v) }
                    }
                }
            }
            argument("sublayout") {
                generalExpression("nil")
                val iterator = element.childNodes.elementIterator()
                if (iterator.hasNext()) {
                    val child = iterator.next()
                }
            }
        }
    }
}
