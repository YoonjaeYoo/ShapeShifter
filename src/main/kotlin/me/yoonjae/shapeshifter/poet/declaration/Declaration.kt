package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.statement.Statement
import me.yoonjae.shapeshifter.poet.type.Type

interface Declaration : Statement

interface DeclarationDescriber : ImportDescriber, ConstantDescriber, VariableDescriber,
        TypeAliasDescriber, FunctionDescriber, InitializerDescriber, EnumDescriber,
        StructDescriber, ClassDescriber {

    val declarations: MutableList<Declaration>

    class Delegate : DeclarationDescriber,
            ImportDescriber by ImportDescriber.Delegate(),
            ConstantDescriber by ConstantDescriber.Delegate(),
            VariableDescriber by VariableDescriber.Delegate(),
            TypeAliasDescriber by TypeAliasDescriber.Delegate(),
            FunctionDescriber by FunctionDescriber.Delegate(),
            InitializerDescriber by InitializerDescriber.Delegate(),
            EnumDescriber by EnumDescriber.Delegate(),
            StructDescriber by StructDescriber.Delegate(),
            ClassDescriber by ClassDescriber.Delegate() {

        override val declarations = mutableListOf<Declaration>()

        override fun import(name: String, init: (Import.() -> Unit)?): Import {
            return super<DeclarationDescriber>.import(name, init).also { declarations.add(it) }
        }

        override fun constant(name: String, value: String, type: String?,
                              init: (Constant.() -> Unit)?): Constant {
            return super<DeclarationDescriber>.constant(name, value, type, init).
                    also { declarations.add(it) }
        }

        override fun variable(name: String, value: String?, type: String?,
                              init: (Variable.() -> Unit)?): Variable {
            return super<DeclarationDescriber>.variable(name, value, type, init).
                    also { declarations.add(it) }
        }

        override fun typeAlias(name: String, type: String, init: (TypeAlias.() -> Unit)?): TypeAlias {
            return super<DeclarationDescriber>.typeAlias(name, type, init).
                    also { declarations.add(it) }
        }

        override fun function(name: String, result: Type?, init: (Function.() -> Unit)?): Function {
            return super<DeclarationDescriber>.function(name, result, init).
                    also { declarations.add(it) }
        }

        override fun initializer(init: (Initializer.() -> Unit)?): Initializer {
            return super<DeclarationDescriber>.initializer(init).also { declarations.add(it) }
        }

        override fun enum(name: String, init: (Enum.() -> Unit)?): Enum {
            return super<DeclarationDescriber>.enum(name, init).
                    also { declarations.add(it) }
        }

        override fun struct(name: String, init: (Struct.() -> Unit)?): Struct {
            return super<DeclarationDescriber>.struct(name, init).
                    also { declarations.add(it) }
        }
    }
}

