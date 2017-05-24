package me.yoonjae.shapeshifter.translator.extensions

import org.w3c.dom.Element

val Element.layoutType: String
    get() {
        return when (tagName) {
            "LinearLayout" -> "LinearLayout"
            "View" -> "View"
            "TextView" -> "TextView"
            "Button" -> "Button"
            "ImageView" -> "ImageView"
            else -> "FrameLayout"
        }
    }

val Element.viewType: String
    get() {
        return when (tagName) {
            "TextView" -> "UILabel"
            "Button" -> "UIButton"
            "ImageView" -> "UIImageView"
            else -> "UIView"
        }
    }

fun Element.attr(name: String): String? {
    if (hasAttribute(name)) {
        val attr = getAttribute(name)
        if (attr.isNotEmpty()) {
            return attr
        }
    }
    return null
}

fun Element.id(): String? {
    return attr("android:id")?.let {
        listOf("@android:id/", "@+id/", "@id/").forEach { prefix ->
            if (it.startsWith(prefix)) {
                return@let it.substring(prefix.length)
            }
        }
        null
    }
}

fun Element.style(): String? {
    return attr("style")?.let {
        if (it.startsWith("@style/")) {
            it.substring(7)
        } else {
            null
        }
    }
}

fun Element.layoutMargin(): Map<String, String>? {
    val params = mutableMapOf("top" to "0", "left" to "0", "bottom" to "0", "right" to "0")
    params.keys.forEach { key ->
        attr("android:layout_margin${key.capitalize()}")?.let {
            params[key] = it.toDimen()
        }
    }
    attr("android:layout_margin")?.let {
        params.keys.forEach { key ->
            params[key] = it.toDimen()
        }
    }
    if (params.values.all { it == "0" }) {
        return null
    }
    return params
}

fun Element.padding(): Map<String, String>? {
    val params = mutableMapOf("top" to "0", "left" to "0", "bottom" to "0", "right" to "0")
    params.keys.forEach { key ->
        attr("android:padding${key.capitalize()}")?.let {
            params[key] = it.toDimen()
        }
    }
    attr("android:padding")?.let {
        params.keys.forEach { key ->
            params[key] = it.toDimen()
        }
    }
    if (params.values.all { it == "0" }) {
        return null
    }
    return params
}

fun Element.layoutWidth(): String {
    val width = getAttribute("android:layout_width")
    return when (width) {
        "match_parent" -> "MATCH_PARENT"
        "wrap_content" -> "WRAP_CONTENT"
        else -> width.toDimen()
    }
}

fun Element.layoutHeight(): String {
    val height = getAttribute("android:layout_height")
    return when (height) {
        "match_parent" -> "MATCH_PARENT"
        "wrap_content" -> "WRAP_CONTENT"
        else -> height.toDimen()
    }
}
