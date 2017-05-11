package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.Indent
import me.yoonjae.shapeshifter.poet.modifier.AccessLevelModifierDescriber
import me.yoonjae.shapeshifter.poet.writeln
import java.io.Writer

class Protocol(val name: String) : Declaration,
        AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate(),
        TypeInheritanceDescriber by TypeInheritanceDescriber.Delegate(),
        TypeAliasDescriber by TypeAliasDescriber.Delegate(),
        ProtocolPropertyDescriber by ProtocolPropertyDescriber.Delegate(),
        ProtocolMethodDescriber by ProtocolMethodDescriber.Delegate(),
        ProtocolInitializerDescriber by ProtocolInitializerDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("protocol $name")
        if (superTypes.isNotEmpty()) {
            writer.write(": ")
            superTypes.forEachIndexed { index, type ->
                if (index > 0) writer.write(", ")
                type.render(writer, linePrefix)
            }
        }
        writer.writeln(" {")
        typeAliases.render(writer, linePrefix)
        protocolProperties.render(writer, linePrefix)
        protocolMethods.render(writer, linePrefix)
        protocolInitializers.render(writer, linePrefix)
        linePrefix?.render(writer)
        writer.write("}")
    }

    private fun List<Declaration>.render(writer: Writer, linePrefix: Element? = null,
                                         newline: Boolean = false) {
        if (isNotEmpty()) {
            forEach {
                if (newline) writer.writeln()
                val prefix = (Indent(1) + linePrefix).apply { render(writer) }
                it.render(writer, prefix)
                writer.writeln()
            }
        }
    }
}

interface ProtocolDescriber : Describer {

    val protocols: MutableList<Protocol>

    fun protocol(name: String, init: (Protocol.() -> Unit)? = null): Protocol {
        val clazz = Protocol(name)
        init?.invoke(clazz)
        protocols.add(clazz)
        return clazz
    }

    class Delegate : ProtocolDescriber {
        override val protocols = mutableListOf<Protocol>()
    }
}
