package me.yoonjae.shapeshifter.poet.modifier

import me.yoonjae.shapeshifter.poet.Describer

class MutationModifier private constructor(name: String) : Modifier(name) {

    companion object {
        val MUTATING = MutationModifier("mutating")
        val NONMUTATING = MutationModifier("nonmutating")
    }
}

interface MutationModifierDescriber : Describer {

    val mutationModifiers: MutableList<MutationModifier>

    fun mutationModifier(modifier: MutationModifier) {
        mutationModifiers.add(modifier)
    }
}
