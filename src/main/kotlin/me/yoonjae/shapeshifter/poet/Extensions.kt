package me.yoonjae.shapeshifter.poet

fun String.toIosResourceName(): String {
    val prefixBuilder = StringBuilder()
    val builder = StringBuilder()
    var prefix = true
    var initial = true
    for (c in toCharArray()) {
        if (c == '_') {
            if (prefix) prefix = false
            initial = true
        } else if (c == '.') {
            break
        } else {
            val b = if (prefix) prefixBuilder else builder
            b.append(if (initial) c.toUpperCase() else c)
            initial = false
        }
    }
    return (if (builder.isEmpty()) "" else builder.toString()) +
            (if (prefixBuilder.isEmpty()) "" else prefixBuilder.toString())
}

