package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import java.io.Writer

class TypeAlias(val name: String, var type: String) : Declaration(),
        AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier.let {
            render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("typealias $name = $type")
    }
}

interface TypeAliasDescriber : Describer {

    val typeAliases: MutableList<TypeAlias>

    fun typeAlias(name: String, type: String, init: (TypeAlias.() -> Unit)? = null): TypeAlias {
        val typeAlias = TypeAlias(name, type)
        init?.invoke(typeAlias)
        typeAliases.add(typeAlias)
        return typeAlias
    }

    class Delegate : TypeAliasDescriber {
        override val typeAliases = mutableListOf<TypeAlias>()
    }
}
