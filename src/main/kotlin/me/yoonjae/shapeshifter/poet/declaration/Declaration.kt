package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.statement.Statement
import me.yoonjae.shapeshifter.poet.type.Type

abstract class Declaration : Statement()

interface DeclarationDescriber : ImportDescriber, ConstantDescriber, VariableDescriber,
        TypeAliasDescriber, FunctionDescriber, InitializerDescriber, EnumDescriber,
        StructDescriber, ClassDescriber, ProtocolDescriber, ExtensionDescriber {

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
            ClassDescriber by ClassDescriber.Delegate(),
            ProtocolPropertyDescriber by ProtocolPropertyDescriber.Delegate(),
            ProtocolDescriber by ProtocolDescriber.Delegate(),
            ExtensionDescriber by ExtensionDescriber.Delegate() {

        override val declarations = mutableListOf<Declaration>()

        override fun import(name: String, init: (Import.() -> Unit)?): Import {
            return super<DeclarationDescriber>.import(name, init).also { declarations.add(it) }
        }

        override fun constant(name: String, type: Type?, value: String?,
                              init: (Constant.() -> Unit)?): Constant {
            return super<DeclarationDescriber>.constant(name, type, value, init).
                    also { declarations.add(it) }
        }

        override fun variable(name: String, type: Type?, value: String?,
                              init: (Variable.() -> Unit)?): Variable {
            return super<DeclarationDescriber>.variable(name, type, value, init).
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

        override fun initializer(optional: Boolean, init: (Initializer.() -> Unit)?): Initializer {
            return super<DeclarationDescriber>.initializer(optional, init).
                    also {declarations.add (it) }
        }

        override fun enum(name: String, init: (Enum.() -> Unit)?): Enum {
            return super<DeclarationDescriber>.enum(name, init).
                    also { declarations.add(it) }
        }

        override fun struct(name: String, init: (Struct.() -> Unit)?): Struct {
            return super<DeclarationDescriber>.struct(name, init).
                    also { declarations.add(it) }
        }

        override fun clazz(name: String, init: (Class.() -> Unit)?): Class {
            return super<DeclarationDescriber>.clazz(name, init).
                    also { declarations.add(it) }
        }

        override fun protocol(name: String, init: (Protocol.() -> Unit)?): Protocol {
            return super<DeclarationDescriber>.protocol(name, init).
                    also { declarations.add(it) }
        }

        override fun extension(name: String, init: (Extension.() -> Unit)?): Extension {
            return super<DeclarationDescriber>.extension(name, init).
                    also { declarations.add(it) }
        }
    }
}
