package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifier
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import java.io.Writer

class TypeAlias(val name: String, val type: String) : Declaration, AccessLevelModifierDescriber {

    override var accessLevelModifier: AccessLevelModifier? = null

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier.let {
            render(writer)
            writer.write(" ")
        }
        writer.write("typealias $name = $type")
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
