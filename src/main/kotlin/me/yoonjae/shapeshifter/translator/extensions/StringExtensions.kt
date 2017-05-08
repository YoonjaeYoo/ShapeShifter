package me.yoonjae.shapeshifter.translator.extensions

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

fun String.toCamelCase(): String {
    val builder = StringBuilder()
    var underscore = false
    for (c in toCharArray()) {
        if (c == '_') {
            underscore = true
        } else if (underscore) {
            builder.append(c.toUpperCase())
            underscore = false
        } else {
            builder.append(c)
        }
    }
    return builder.toString()
}

fun String.toDimen(): String {
    return if (contains("@dimen/")) {
        "Dimens.${substring(7).toCamelCase()}"
    } else {
        substring(0, length - 2)
    }
}
