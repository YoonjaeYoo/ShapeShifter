package me.yoonjae.shapeshifter.poet.declaration

import java.io.Writer

class TypeAlias(val name: String, val type: String) : Declaration {

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        writer.write("typealias ")
        writer.write(name)
        writer.write(" = ")
        writer.write(type)
    }
}

interface TypeAliasContainer {

    val typeAliases: MutableList<TypeAlias>

    fun variable(name: String, type: String): TypeAlias {
        val typeAlias = TypeAlias(name, type)
        typeAliases.add(typeAlias)
        return typeAlias
    }
}
