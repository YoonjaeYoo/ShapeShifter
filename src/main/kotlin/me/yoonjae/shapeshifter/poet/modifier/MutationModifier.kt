package me.yoonjae.shapeshifter.poet.modifier

import me.yoonjae.shapeshifter.poet.Describer

class MutationModifier private constructor(name: String) : Modifier(name) {

    companion object {
        val MUTATING = MutationModifier("mutating")
        val NONMUTATING = MutationModifier("nonmutating")
    }
}

interface MutationModifierDescriber : Describer {

    var mutationModifier: MutationModifier?

    fun mutating() {
        mutationModifier = MutationModifier.MUTATING
    }

    fun nonmutating() {
        mutationModifier = MutationModifier.NONMUTATING
    }

    class Delegate : MutationModifierDescriber {
        override var mutationModifier: MutationModifier? = null
    }
}
