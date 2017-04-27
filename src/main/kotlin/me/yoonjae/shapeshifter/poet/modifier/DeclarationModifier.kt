package me.yoonjae.shapeshifter.poet.modifier

class DeclarationModifier private constructor(name: String) : Modifier(name) {

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