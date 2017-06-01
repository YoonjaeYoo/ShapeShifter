package me.yoonjae.shapeshifter.translator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import me.yoonjae.shapeshifter.poet.declaration.Class
import me.yoonjae.shapeshifter.poet.expression.FunctionCallExpression
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.parseAndroidType
import me.yoonjae.shapeshifter.translator.extensions.quoted
import me.yoonjae.shapeshifter.translator.extensions.value
import java.io.File

class RestServiceTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile? {
        val name = file.name.split(".").first()
        return SwiftFile("$name.swift") {
            import("UIKit")
            import("Just")

            clazz(name) {
                constant("BASE_URL", value = "Bundle.main.infoDictionary![\"BaseUrl\"] as! String")
                val optional = JavaParser.parse(file).getInterfaceByName(name)
                if (optional.isPresent) {
                    optional.get().methods.forEach { translateMethod(it) }
                }
            }
        }
    }

    private fun Class.translateMethod(method: MethodDeclaration) {
        if (!method.isAnnotationPresent("Multipart")) {
            function(method.nameAsString) {
                method.parameters.forEach {
                    parameter(it.nameAsString,
                            it.type.toString().parseAndroidType(it.headerKey() == null &&
                                    it.pathKey() == null && it.queryKey() == null)
                    )
                }
                parameter("completionHandler", Type("(HTTPResult) -> Void", true))

                variable("json") {
                    initializerExpression("Dictionary<String, Any>")
                }
                method.parameters.forEach { parameter ->
                    parameter.jsonKey()?.let {
                        ifStatement("${parameter.nameAsString} != nil") {
                            codeBlock {
                                assignmentExpression("json[${it.quoted()}]", parameter.value())
                            }
                        }
                    }
                }

                val httpMethod = listOf("GET", "POST", "PUT", "PATCH", "DELETE").
                        filter { method.isAnnotationPresent(it) }.first()
                functionCallExpression("Just.${httpMethod.toLowerCase()}") {
                    argument(value = "BASE_URL + " + path(method, httpMethod))
                    paramArgument(method)
                    argument("json", "json")
                    headerArgument(method)
                    argument("asyncCompletionHandler", "completionHandler")
                }
            }
        }
    }

    private fun path(method: MethodDeclaration, httpMethod: String): String {
        var path = method.getAnnotationByName(httpMethod).get().value()
        method.parameters.forEach { parameter ->
            parameter.pathKey()?.let {
                path = path.replace("{$it}", "\\(${parameter.value()})")
            }
        }
        return path.quoted()
    }

    private fun FunctionCallExpression.paramArgument(method: MethodDeclaration) {
        argument("params") {
            dictionaryLiteralExpression {
                method.parameters.forEach { parameter ->
                    parameter.queryKey()?.let {
                        keyValue(it.quoted(), parameter.value())
                    }
                }
            }
        }
    }

    private fun FunctionCallExpression.headerArgument(method: MethodDeclaration) {
        argument("headers") {
            dictionaryLiteralExpression {
                method.parameters.forEach { parameter ->
                    parameter.headerKey()?.let {
                        keyValue(it.quoted(), parameter.value())
                    }
                }
            }
        }
    }

    private fun Parameter.headerKey(): String? = if (isAnnotationPresent("Header")) {
        getAnnotationByName("Header").get().value()
    } else {
        null
    }

    private fun Parameter.queryKey(): String? = if (isAnnotationPresent("Query")) {
        getAnnotationByName("Query").get().value()
    } else {
        null
    }

    private fun Parameter.pathKey(): String? = if (isAnnotationPresent("Path")) {
        getAnnotationByName("Path").get().value()
    } else {
        null
    }

    private fun Parameter.jsonKey(): String? = if (isAnnotationPresent("Header") ||
            isAnnotationPresent("Query") || isAnnotationPresent("Path")) {
        null
    } else if (isAnnotationPresent("Field")) {
        getAnnotationByName("Field").get().value()
    } else {
        nameAsString
    }

    private fun Parameter.value(): String = if (isAnnotationPresent("Body")) {
        "$nameAsString?.properties()"
    } else if (headerKey() == "Authorization") {
        "Token: \\($nameAsString)".quoted()
    } else {
        nameAsString
    }
}
