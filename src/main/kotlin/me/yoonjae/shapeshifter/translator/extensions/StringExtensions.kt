package me.yoonjae.shapeshifter.translator.extensions

fun String.toConfigParameterName() = "config${toResourceName(true)}"

fun String.toResourceName(capital: Boolean = false): String {
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
    val name = (if (builder.isEmpty()) "" else builder.toString()) +
            (if (prefixBuilder.isEmpty()) "" else prefixBuilder.toString())
    return if (capital) name.capitalize() else name.decapitalize()
}

fun String.toCamelCase(capital: Boolean = false): String {
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
    val camelCase = builder.toString()
    return if (capital) camelCase.capitalize() else camelCase.decapitalize()
}

fun String.toDimen(): String {
    return if (contains("@dimen/")) {
        "Dimen.${substring(7).toCamelCase()}"
    } else if (isNotEmpty()) {
        substring(0, length - 2) // for trimming dp
    } else {
        "0"
    }
}

fun String.toColor(): String {
    return if (contains("@color/")) {
        "Color.${substring(7).toCamelCase()}"
    } else if (isNotEmpty()) {
        "UIColor(\"$this\")"
    } else {
        "UIColor.clear"
    }
}

fun String.toDrawable(): String {
    return if (contains("@color/")) {
        "Drawable.color(Color.${substring(7)})"
    } else if (startsWith("#")) {
        "Drawable.color(UIColor(\"$this\"))"
    } else {
        "Drawable.image(UIImage(named: \"${substring(10)}\"))"
    }
}
