package me.yoonjae.shapeshifter.poet.modifier

class AccessLevelModifier private constructor(name: String) : Modifier(name) {

    companion object {
        val PRIVATE = AccessLevelModifier("private")
        val FILEPRIVATE = AccessLevelModifier("fileprivate")
        val INTERNAL = AccessLevelModifier("internal")
        val PUBLIC = AccessLevelModifier("public")
        val OPEN = AccessLevelModifier("open")
    }
}
