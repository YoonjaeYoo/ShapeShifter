package me.yoonjae.shapeshifter.translator

import java.io.File

abstract class Translator<out F : me.yoonjae.shapeshifter.poet.file.File> {

    abstract fun translate(file: File): F
}
