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
                    parameter("alignment", Type("Alignment"), ".fill")
                    parameter("viewReuseId", Type("String?"), "nil")
                    parameter("sublayouts", Type("[Layout]"))
                    parameter("config", Type("((V) -> Void)?"), "nil")

                    generalExpression("self.sublayouts = sublayouts")
                    initializerExpression("super") {
                        argument("alignment", "alignment")
                        argument("flexibility", ".flexible")
                        argument("viewReuseId", "viewReuseId")
                        argument("config", "config")
                    }
                }

                function("measurement", Type("LayoutMeasurement")) {
                    open()
                    parameter("maxSize", Type("CGSize"), label = "within")

                    constant("size", "maxSize")
                    returnStatement {
                        initializerExpression("LayoutMeasurement") {
                            argument("layout", "self")
                            argument("size", "size")
                            argument("maxSize", "maxSize")
                            argument("sublayouts") {
                                functionCallExpression("sublayouts.map") {
                                    trailingClosure {
                                        closureParameter("sublayout")
                                        functionCallExpression("sublayout.measurement") {
                                            argument("within", "size")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                function("arrangement", Type("LayoutArrangement")) {
                    open()
                    parameter("rect", Type("CGRect"), label = "within")
                    parameter("measurement", Type("LayoutMeasurement"))

                    constant("frame") {
                        functionCallExpression("alignment.position") {
                            argument("size", "measurement.size")
                            argument("in", "rect")
                        }
                    }
                    constant("sublayoutRect") {
                        initializerExpression("CGRect") {
                            argument("x", "0")
                            argument("y", "0")
                            argument("width", "frame.width")
                            argument("height", "frame.height")
                        }
                    }
                    constant("sublayouts") {
                        functionCallExpression("measurement.sublayouts.map") {
                            trailingClosure {
                                closureParameter("measurement")
                                returnStatement {
                                    functionCallExpression("measurement.arrangement") {
                                        argument("within", "sublayoutRect")
                                    }
                                }
                            }
                        }
                    }
                    returnStatement {
                        initializerExpression("LayoutArrangement") {
                            argument("layout", "self")
                            argument("frame", "frame")
                            argument("sublayouts", "sublayouts")
                        }
                    }
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
                    element.insets().forEach { k, v -> argument(k, v) }
                }
            }
            argument("alignment") {
                initializerExpression("Alignment") {
                    element.alignment().forEach { k, v -> argument(k, v) }
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
