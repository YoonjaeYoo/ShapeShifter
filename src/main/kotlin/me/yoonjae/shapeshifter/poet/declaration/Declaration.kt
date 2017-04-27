package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

interface Declaration : Element

interface DeclarationContainer : ImportContainer, ConstantContainer, VariableContainer,
        TypeAliasContainer, FunctionContainer, InitializerContainer, EnumContainer, StructContainer,
        ClassContainer

fun List<Declaration>.render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
    if (isNotEmpty()) {
        forEach {
            it.render(writer, beforeEachLine)
        }
        writer.writeln("")
    }
}