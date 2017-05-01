package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import java.io.Writer

class TypeAlias(val name: String, val type: String) : Declaration, AccessLevelModifierDescriber {

    override val accessLevelModifiers = mutableListOf<AccessLevelModifier>()

    override fun render(writer: Writer, beforeEachLine: ((Writer) -> Unit)?) {
        beforeEachLine?.invoke(writer)
        accessLevelModifiers.forEach { it.render(writer) }
        writer.write("typealias ")
        writer.write(name)
        writer.write(" = ")
        writer.write(type)
    }
}

interface TypeAliasDescriber : Describer {

    val typeAliases: MutableList<TypeAlias>

    fun variable(name: String, type: String): TypeAlias {
        val typeAlias = TypeAlias(name, type)
        typeAliases.add(typeAlias)
        return typeAlias
    }
}
