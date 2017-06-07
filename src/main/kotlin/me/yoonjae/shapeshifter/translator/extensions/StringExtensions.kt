package me.yoonjae.shapeshifter.translator.extensions

import me.yoonjae.shapeshifter.poet.type.Type

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

fun String.toSnakeCase(): String {
    val builder = StringBuilder()
    for (c in toCharArray()) {
        if (c.isUpperCase()) {
            if (builder.isNotEmpty()) {
                builder.append("_")
            }
            builder.append(c.toLowerCase())
        } else {
            builder.append(c)
        }
    }
    return builder.toString()
}

private val WRAPPER_TYPE_MAP = mapOf(
        "List<(.+)>" to "[$1]"
)
private val TYPE_MAP = mapOf(
        "Integer" to "Int",
        "Long" to "Int64",
        "Boolean" to "Bool"
)

fun String.parseAndroidType(optional: Boolean = false): Type {
    var type = this
    listOf(WRAPPER_TYPE_MAP, TYPE_MAP).forEach { map ->
        type = map.filter {
            type.contains(it.key.toRegex())
        }.entries.firstOrNull()?.let {
            type.replace(it.key.toRegex(), it.value)
        } ?: type
    }
    return Type(type, optional)
}

fun String.parseXmlString(): String {
    return if (startsWith("@string/")) {
        "\"${substring(8)}\".localized()"
    } else {
        "\"$this\""
    }
}

fun String.parseXmlDimen(): String {
    return if (startsWith("@dimen/")) {
        "Dimen.${substring(7).toCamelCase()}"
    } else if (endsWith("dp") || endsWith("sp")) {
        substring(0, length - 2)
    } else {
        "0"
    }
}

fun String.parseXmlColor(): String {
    return if (startsWith("@color/")) {
        "Color.${substring(7).toCamelCase()}"
    } else if (startsWith("@drawable/")) {
        "UIColor(patternImage: UIImage(named: \"${substring(10)}\")!)"
    } else if (startsWith("#")) {
        val color = substring(1)
        if (color.length == 4) {
            "UIColor(\"#${color.substring(1)}${color[0]}\")"
        } else if (color.length == 8) {
            "UIColor(\"#${color.substring(2)}${color.substring(0, 2)}\")"
        } else {
            "UIColor(\"#$color\")"
        }
    } else {
        "UIColor.clear"
    }
}

fun String.parseXmlImage(): String {
    return if (startsWith("@drawable/")) {
        "UIImage(named: \"${substring(10)}\")"
    } else {
        "UIImage()"
    }
}

fun String.parseXmlGravity(): String {
    val builder = StringBuilder("[")
    split("|").forEachIndexed { index, gravity ->
        if (index > 0) {
            builder.append(", ")
        }
        builder.append(".${gravity.toCamelCase()}")
    }
    builder.append("]")
    return builder.toString()
}

fun String.quoted(): String = "\"$this\""

fun String.unquoted(): String = substring(
        if (this[0] == '"') 1 else 0,
        if (this[length - 1] == '"') length - 1 else length
)
