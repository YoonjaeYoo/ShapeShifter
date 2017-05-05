package me.yoonjae.shapeshifter.poet.modifier

import me.yoonjae.shapeshifter.poet.Describer

class AccessLevelModifier private constructor(name: String) : Modifier(name) {

    companion object {
        val PRIVATE = AccessLevelModifier("private")
        val FILEPRIVATE = AccessLevelModifier("fileprivate")
        val INTERNAL = AccessLevelModifier("internal")
        val PUBLIC = AccessLevelModifier("public")
        val OPEN = AccessLevelModifier("open")
    }
}

interface AccessLevelModifierDescriber : Describer {

    var accessLevelModifier: AccessLevelModifier?

    class Delegate : AccessLevelModifierDescriber {
        override var accessLevelModifier: AccessLevelModifier? = null
    }
}
