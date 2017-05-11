package me.yoonjae.shapeshifter.poet.modifier

import me.yoonjae.shapeshifter.poet.Describer

class DeclarationModifier constructor(name: String) : Modifier(name) {

    companion object {
        val CLASS = DeclarationModifier("class")
        val CONVENIENCE = DeclarationModifier("convenience")
        val DYNAMIC = DeclarationModifier("dynamic")
        val FINAL = DeclarationModifier("final")
        val INFIX = DeclarationModifier("infix")
        val LAZY = DeclarationModifier("lazy")
        val OPTIONAL = DeclarationModifier("optional")
        val OVERRIDE = DeclarationModifier("override")
        val POSTFIX = DeclarationModifier("postfix")
        val PREFIX = DeclarationModifier("prefix")
        val REQUIRED = DeclarationModifier("required")
        val STATIC = DeclarationModifier("static")
        val UNOWNED = DeclarationModifier("unowned")
        val WEAK = DeclarationModifier("weak")
    }
}

interface DeclarationModifierDescriber : Describer, AccessLevelModifierDescriber,
        MutationModifierDescriber {

    val declarationModifiers: MutableList<DeclarationModifier>

    fun clazz() = declarationModifiers.add(DeclarationModifier.CLASS)
    fun convenience() = declarationModifiers.add(DeclarationModifier.CONVENIENCE)
    fun dynamic() = declarationModifiers.add(DeclarationModifier.DYNAMIC)
    fun final() = declarationModifiers.add(DeclarationModifier.FINAL)
    fun infix() = declarationModifiers.add(DeclarationModifier.INFIX)
    fun lazy() = declarationModifiers.add(DeclarationModifier.LAZY)
    fun optional() = declarationModifiers.add(DeclarationModifier.OPTIONAL)
    fun override() = declarationModifiers.add(DeclarationModifier.OVERRIDE)
    fun postfix() = declarationModifiers.add(DeclarationModifier.POSTFIX)
    fun prefix() = declarationModifiers.add(DeclarationModifier.PREFIX)
    fun required() = declarationModifiers.add(DeclarationModifier.REQUIRED)
    fun static() = declarationModifiers.add(DeclarationModifier.STATIC)
    fun unowned() = declarationModifiers.add(DeclarationModifier.UNOWNED)
    fun weak() = declarationModifiers.add(DeclarationModifier.WEAK)

    class Delegate : DeclarationModifierDescriber,
            AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate(),
            MutationModifierDescriber by MutationModifierDescriber.Delegate() {
        override val declarationModifiers = mutableListOf<DeclarationModifier>()
    }
}
