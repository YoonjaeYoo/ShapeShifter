package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.declaration.*
import me.yoonjae.shapeshifter.poet.declaration.Enum
import me.yoonjae.shapeshifter.poet.declaration.Function
import java.io.Writer

class SwiftFile : File, ImportContainer, ConstantContainer, VariableContainer,
        TypeAliasContainer, FunctionContainer, EnumContainer, StructContainer, ClassContainer {

    companion object {
        fun create(init: SwiftFile.() -> Unit): SwiftFile {
            val file = SwiftFile()
            file.init()
            return file
        }
    }

    override val imports = mutableListOf<Import>()
    override val typeAliases = mutableListOf<TypeAlias>()
    override val constants = mutableListOf<Constant>()
    override val variables = mutableListOf<Variable>()
    override val functions = mutableListOf<Function>()
    override val enums = mutableListOf<Enum>()
    override val structs = mutableListOf<Struct>()
    override val classes = mutableListOf<Class>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        imports.render(writer, beforeEachLine)
        typeAliases.render(writer, beforeEachLine)
        constants.render(writer, beforeEachLine)
        variables.render(writer, beforeEachLine)
        functions.render(writer, beforeEachLine)
        enums.render(writer, beforeEachLine)
        structs.render(writer, beforeEachLine)
        classes.render(writer, beforeEachLine)
    }
}