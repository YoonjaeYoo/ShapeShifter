package me.yoonjae.shapeshifter.translator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.EnumDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import me.yoonjae.shapeshifter.poet.declaration.ClassDescriber
import me.yoonjae.shapeshifter.poet.file.SwiftFile
import me.yoonjae.shapeshifter.poet.type.Type
import me.yoonjae.shapeshifter.translator.extensions.parseAndroidType
import me.yoonjae.shapeshifter.translator.extensions.quoted
import me.yoonjae.shapeshifter.translator.extensions.toSnakeCase
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
            } else {
                superType("Mappable")
            }
            clazz.fields.filterNot { it.isStatic }.forEach {
                val variable = it.getVariable(0)
                val type = variable.type.toString().parseAndroidType(true)
                variable(variable.nameAsString, type)
            }

            if (extending) {
                initializer {
                    override()
                    initializerExpression("super")
                }
            }

            initializer {
                required()
                parameter("map", Type("[String: Any]"), label = "_")
                if (extending) {
                    initializerExpression("super") {
                        argument(value = "map")
                    }
                }
                constant("dateTransformer") {
                    closureExpression(Type("Date", true)) {
                        closureParameter("value", Type("String"))
                        constant("dateFormatter", value = "DateFormatter()")
                        assignmentExpression("dateFormatter.dateFormat",
                                "yyyy-MM-dd'T'HH:mm:ss".quoted())
                        returnStatement("dateFormatter.date(from: value)")
                    }
                }
                clazz.fields.filterNot { it.isStatic }.forEach {
                    val fieldName = if (it.isAnnotationPresent("SerializedName")) {
                        it.getAnnotationByName("SerializedName").get().value()
                    } else {
                        it.getVariable(0).nameAsString.toSnakeCase()
                    }
                    if (it.isDate()) {
                        generalExpression("${it.getVariable(0).name} <- " +
                                "map.transform(${fieldName.quoted()}, transformer: dateTransformer)")
                    } else {
                        val method = if (it.isEnum()) {
                            "enum"
                        } else if (it.isProperty()) {
                            "property"
                        } else {
                            if (it.isRelations()) {
                                "relations"
                            } else {
                                "relation"
                            }
                        }
                        generalExpression("${it.getVariable(0).name} <- map.$method(${fieldName.quoted()})")
                    }
                }
            }

            clazz.childNodes.forEach {
                if (it is EnumDeclaration) {
                    enum(it.nameAsString) {
                        superType("String")

                        it.entries.forEach {
                            val fieldName = it.nameAsString.toLowerCase()
                            var value = fieldName
                            if (it.isAnnotationPresent("SerializedName")) {
                                value = it.getAnnotationByName("SerializedName").get().value()
                            }
                            case(fieldName, value.quoted())
                        }
                    }
                } else if (it is ClassOrInterfaceDeclaration) {
                    describeClass(it)
                }
            }
        }
    }

    private fun FieldDeclaration.isEnum(): Boolean = elementType.toString().contains("Enum")

    private fun FieldDeclaration.isProperty(): Boolean {
        val primitiveTypes = listOf("Byte", "Short", "Integer", "Long", "Float", "Double",
                "Boolean", "Char", "String")
        return primitiveTypes.filter {
            elementType.toString().contains(it)
        }.isNotEmpty()
    }

    private fun FieldDeclaration.isRelations(): Boolean =
            elementType.arrayLevel > 0 || elementType.toString().contains("List")

    private fun FieldDeclaration.isDate(): Boolean = elementType.toString() == "Date"
}