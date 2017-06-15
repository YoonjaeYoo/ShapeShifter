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
import me.yoonjae.shapeshifter.translator.extensions.urlEncoded
import me.yoonjae.shapeshifter.translator.extensions.value
import java.io.File

class RestServiceTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile? {
        val name = file.name.split(".").first()
        return SwiftFile("$name.swift") {
            import("UIKit")
            import("Just")
            import("Tailor")

            clazz(name) {
                constant("BASE_URL", value = "Bundle.main.infoDictionary![\"BaseUrl\"] as! String")
                val optional = JavaParser.parse(file).getInterfaceByName(name)
                if (optional.isPresent) {
                    optional.get().methods.forEach { translateMethod(it) }
                }

                clazz("ListResponse") {
                    genericParameter("T") {
                        superType("Mappable")
                    }
                    superType("Mappable")

                    variable("count", Type("Int", true))
                    variable("next", Type("String", true))
                    variable("previous", Type("String", true))
                    variable("results", Type("[T]", true))

                    initializer {
                        required()
                        parameter("map", Type("[String: Any]"), label = "_")
                        listOf("count", "next", "previous").forEach {
                            generalExpression("$it <- map.property(${it.quoted()})")
                        }
                        generalExpression("results <- map.relations(\"results\")")
                    }
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
                parameter("progressHandler", Type("(HTTPProgress) -> Void", true), "nil")
                parameter("completionHandler", Type("(HTTPResult) -> Void", true), "nil")

                val json = method.parameters.filter { it.jsonKey() != null }
                if (json.isNotEmpty()) {
                    variable("json") {
                        initializerExpression("Dictionary<String, Any>")
                    }
                    method.parameters.forEach { parameter ->
                        parameter.jsonKey()?.let {
                            ifStatement("${parameter.nameAsString} != nil") {
                                assignmentExpression("json[${it.quoted()}]", parameter.value())
                            }
                        }
                    }
                }

                val httpMethod = listOf("GET", "POST", "PUT", "PATCH", "DELETE").
                        filter { method.isAnnotationPresent(it) }.first()
                functionCallExpression("Just.${httpMethod.toLowerCase()}") {
                    argument(value = "BASE_URL + " + path(method, httpMethod))
                    paramArgument(method, httpMethod)
                    if (json.isNotEmpty()) {
                        argument("json", "json")
                    }
                    headerArgument(method)
                    argument("asyncProgressHandler", "progressHandler")
                    argument("asyncCompletionHandler", "completionHandler")
                }
            }
        }
    }

    private fun path(method: MethodDeclaration, httpMethod: String): String {
        var path = method.getAnnotationByName(httpMethod).get().value().split('?')[0]
        method.parameters.forEach { parameter ->
            parameter.pathKey()?.let {
                path = path.replace("{$it}", "\\(${parameter.urlEncodedValue()})")
            }
        }
        return path.quoted()
    }

    private fun FunctionCallExpression.paramArgument(method: MethodDeclaration, httpMethod: String) {
        val params = method.parameters.filter { it.queryKey() != null }
        if (params.isNotEmpty()) {
            argument("params") {
                dictionaryLiteralExpression {
                    val path = method.getAnnotationByName(httpMethod).get().value()
                    if (path.contains('?')) {
                        path.split('?')[1].split('&').forEach {
                            val keyValue = it.split('=')
                            keyValue(keyValue[0].quoted(), keyValue[1].quoted())
                        }
                    }
                    params.forEach { parameter ->
                        parameter.queryKey()?.let {
                            keyValue(it.quoted(), parameter.urlEncodedValue())
                        }
                    }
                }
            }
        }
    }

    private fun FunctionCallExpression.headerArgument(method: MethodDeclaration) {
        val headers = method.parameters.filter { it.headerKey() != null }
        if (headers.isNotEmpty()) {
            argument("headers") {
                dictionaryLiteralExpression {
                    headers.forEach { parameter ->
                        parameter.headerKey()?.let {
                            keyValue(it.quoted(), parameter.value())
                        }
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
        "Token \\($nameAsString)".quoted()
    } else {
        nameAsString
    }

    private fun Parameter.urlEncodedValue(): String = if (type.toString() == "String") {
        nameAsString.urlEncoded()
    } else {
        nameAsString
    }
}
