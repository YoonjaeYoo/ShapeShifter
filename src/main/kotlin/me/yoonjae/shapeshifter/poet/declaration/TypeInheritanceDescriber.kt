package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.type.Type

interface TypeInheritanceDescriber {

    val superTypes: MutableList<Type>

    fun superType(name: String, init: (Type.() -> Unit)? = null): Type {
        val superType = Type(name)
        init?.invoke(superType)
        superTypes.add(superType)
        return superType
    }

    class Delegate : TypeInheritanceDescriber {
        override val superTypes = mutableListOf<Type>()
    }
}
