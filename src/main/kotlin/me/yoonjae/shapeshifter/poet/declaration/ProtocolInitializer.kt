package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import java.io.Writer

class ProtocolInitializer : Declaration(),
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        ParameterDescriber by ParameterDescriber.Delegate() {

    override fun render(writer: Writer, linePrefix: Element?) {
        accessLevelModifier?.let {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        declarationModifiers.forEach {
            it.render(writer, linePrefix)
            writer.write(" ")
        }
        writer.write("init")
        parameters.render(writer, linePrefix)
    }
}

interface ProtocolInitializerDescriber : Describer {

    val protocolInitializers: MutableList<ProtocolInitializer>

    fun protocolInitializer(init: (ProtocolInitializer.() -> Unit)? = null):
            ProtocolInitializer {
        val protocolInitializer = ProtocolInitializer()
        init?.invoke(protocolInitializer)
        protocolInitializers.add(protocolInitializer)
        return protocolInitializer
    }

    class Delegate : ProtocolInitializerDescriber {
        override val protocolInitializers = mutableListOf<ProtocolInitializer>()
    }
}
