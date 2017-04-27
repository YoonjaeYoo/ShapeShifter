package me.yoonjae.shapeshifter.poet.modifier

class MutationModifier private constructor(name: String) : Modifier(name) {

    companion object {
        val MUTATING = MutationModifier("mutating")
        val NONMUTATING = MutationModifier("nonmutating")
    }
}
