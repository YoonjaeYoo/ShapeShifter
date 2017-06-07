package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class ProtocolProperty(val name: String, var type: Type) : Declaration(),
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate() {

    var get: GetKeyword = GetKeyword()
    var set: SetKeyword? = null

    fun get(init: (GetKeyword.() -> Unit)? = null) {
        val get = GetKeyword()
        init?.invoke(get)
        this.get = get
    }

    fun set(init: (SetKeyword.() -> Unit)? = null) {
        val set = SetKeyword()
        init?.invoke(set)
        this.set = set
    }

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("var ")
        writer.write(name)
        writer.write(": ")
        type.render(writer)
        writer.write(" { ")
        get.render(writer)
        writer.write(" ")
        set?.let {
            it.render(writer)
            writer.write(" ")
        }
        writer.write("}")
    }
}

interface ProtocolPropertyDescriber : Describer {

    val protocolProperties: MutableList<ProtocolProperty>

    fun protocolProperty(name: String, type: Type,
                         init: (ProtocolProperty.() -> Unit)? = null): ProtocolProperty {
        val protocolProperty = ProtocolProperty(name, type)
        init?.invoke(protocolProperty)
        protocolProperties.add(protocolProperty)
        return protocolProperty
    }

    class Delegate : ProtocolPropertyDescriber {
        override val protocolProperties = mutableListOf<ProtocolProperty>()
    }
}
