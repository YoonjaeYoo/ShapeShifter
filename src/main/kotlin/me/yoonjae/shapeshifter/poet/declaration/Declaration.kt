package me.yoonjae.shapeshifter.poet.declaration

import me.yoonjae.shapeshifter.poet.Element

interface Declaration : Element

interface DeclarationDescriber : ImportDescriber, ConstantDescriber, VariableDescriber,
        TypeAliasDescriber, FunctionDescriber, InitializerDescriber, EnumDescriber,
        StructDescriber, ClassDescriber
