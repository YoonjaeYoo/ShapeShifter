package me.yoonjae.shapeshifter.poet.file

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.declaration.*
import me.yoonjae.shapeshifter.poet.declaration.Enum
import me.yoonjae.shapeshifter.poet.declaration.Function
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class SwiftFile : File, ImportDescriber, ConstantDescriber, VariableDescriber,
        TypeAliasDescriber, FunctionDescriber, EnumDescriber, StructDescriber, ClassDescriber {

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

    override fun render(writer: Writer, linePrefix: Element?) {
        imports.render(writer)
        typeAliases.render(writer)
        constants.render(writer)
        variables.render(writer)
        functions.render(writer)
        enums.render(writer)
        structs.render(writer)
        classes.render(writer)
    }

    private fun List<Declaration>.render(writer: Writer, linePrefix: Element? = null) {
        if (isNotEmpty()) {
            linePrefix?.render(writer)
            forEach {
                it.render(writer, linePrefix)
                writer.writeln()
            }
            writer.writeln()
        }
    }
}