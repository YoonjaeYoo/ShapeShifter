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

interface DeclarationModifierDescriber : Describer, AccessLevelModifierDescriber {

    val declarationModifiers: MutableList<DeclarationModifier>

    fun declarationModifier(modifier: DeclarationModifier) {
        declarationModifiers.add(modifier)
    }

    class Delegate : DeclarationModifierDescriber,
            AccessLevelModifierDescriber by AccessLevelModifierDescriber.Delegate() {
        override val declarationModifiers = mutableListOf<DeclarationModifier>()
    }
}
