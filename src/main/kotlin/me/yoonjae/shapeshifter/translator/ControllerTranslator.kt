package me.yoonjae.shapeshifter.translator

import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.toResourceName
import java.io.File

class ControllerTranslator(val activityThemeMap: Map<String, String>) : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile {
        val text = file.readText()
        var resourceName = file.name.substring(0, file.name.lastIndexOf('.'))
        return SwiftFile("$resourceName.swift") {
            import("UIKit")
            import("LayoutKit")

            clazz(resourceName) {
                superType("BaseViewController") {
                    initializer {
                        initializerExpression("super") {
                            argument("theme",
                                    activityThemeMap.getOrDefault(resourceName, "AppTheme") + "()")
                        }
                    }

                    initializer(true) {
                        required()
                        convenience()
                        parameter("aDecoder", Type("NSCoder"), label = "coder")

                        initializerExpression("self")
                    }

                    if (text.contains("@Inject") &&
                            !text.contains("@Inject(value = R.class, base = true)")) {
                        parseResourceName(text)?.let {
                            resourceName = it
                        }
                        function("layout", Type("Layout")) {
                            override()
                            returnStatement("${resourceName}Layout()")
                        }
                    }
                }
            }
        }
    }

    private fun parseResourceName(text: String): String? {
        val pattern = "@Inject(value = R.class, layoutId = "
        val index = text.indexOf(pattern)
        if (index > -1) {
            val resourceId = text.substring(index + pattern.length,
                    text.indexOf(")", index))
            if (resourceId.startsWith("R.layout.")) {
                return resourceId.substring(9).toResourceName(true)
            }
        }
        return null
    }
}