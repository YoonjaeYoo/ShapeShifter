package me.yoonjae.shapeshifter.translator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.EnumDeclaration
import me.yoonjae.shapeshifter.poet.declaration.ClassDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.quoted
import me.yoonjae.shapeshifter.translator.extensions.toSnakeCase
import me.yoonjae.shapeshifter.translator.extensions.parseAndroidType
import me.yoonjae.shapeshifter.translator.extensions.value
import java.io.File

class ModelTranslator : Translator<SwiftFile>() {

    override fun translate(file: File): SwiftFile? {
        val modelName = file.name.split(".").first()
        return SwiftFile("$modelName.swift") {
            import("Foundation")
            import("Tailor")

            val optional = JavaParser.parse(file).getClassByName(modelName)
            if (optional.isPresent) {
                describeClass(optional.get())
            }
        }
    }

    private fun ClassDescriber.describeClass(clazz: ClassOrInterfaceDeclaration) {
        clazz(clazz.nameAsString) {
            val extending = clazz.extendedTypes.isNotEmpty()
            if (extending) {
                superType(clazz.getExtendedTypes(0).nameAsString)
            }
            clazz.fields.filterNot { it.isStatic }.forEach {
                val variable = it.getVariable(0)
                val type = variable.type.toString().parseAndroidType(true)
                variable(variable.nameAsString, type)
            }

            initializer {
                required()
                parameter("map", Type("[String: Any]"), label = "_")
                if (extending) {
                    initializerExpression("super") {
                        argument(value = "map")
                    }
                }
                clazz.fields.filterNot { it.isStatic }.forEach {
                    var name = it.getVariable(0).nameAsString
                    if (it.isAnnotationPresent("SerializedName")) {
                        name = it.getAnnotationByName("SerializedName").get().value()
                    } else {
                        name = name.toSnakeCase()
                    }
                    generalExpression("${it.getVariable(0).name} <- map.property(${name.quoted()})")
                }
            }

            clazz.childNodes.forEach {
                if (it is EnumDeclaration) {
                    enum(it.nameAsString) {
                        superType("String")

                        it.entries.forEach {
                            val name = it.nameAsString.toLowerCase()
                            var value = name
                            if (it.isAnnotationPresent("SerializedName")) {
                                value = it.getAnnotationByName("SerializedName").get().value()
                            }
                            case(name, value.quoted())
                        }
                    }
                } else if (it is ClassOrInterfaceDeclaration) {
                    describeClass(it)
                }
            }
        }
    }
}