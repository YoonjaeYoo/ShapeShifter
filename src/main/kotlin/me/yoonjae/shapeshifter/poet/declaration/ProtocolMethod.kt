package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Describer
import me.yoonjae.shapeshifter.poet.Element
import me.yoonjae.shapeshifter.poet.modifier.DeclarationModifierDescriber
import me.yoonjae.shapeshifter.poet.type.Type
import java.io.Writer

class ProtocolMethod(val name: String, var result: Type? = null) : Declaration(),
        DeclarationModifierDescriber by DeclarationModifierDescriber.Delegate(),
        GenericParameterDescriber by GenericParameterDescriber.Delegate(),
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
        writer.write("func $name")
        genericParameters.render(writer, linePrefix)
        parameters.render(writer, linePrefix)
        result?.let {
            writer.write(" -> ")
            it.render(writer, linePrefix)
        }
    }
}

interface ProtocolMethodDescriber : Describer {

    val protocolMethods: MutableList<ProtocolMethod>

    fun protocolMethod(name: String, result: Type? = null, init: (ProtocolMethod.() -> Unit)? = null):
            ProtocolMethod {
        val protocolMethod = ProtocolMethod(name, result)
        init?.invoke(protocolMethod)
        protocolMethods.add(protocolMethod)
        return protocolMethod
    }

    class Delegate : ProtocolMethodDescriber {
        override val protocolMethods = mutableListOf<ProtocolMethod>()
    }
}
