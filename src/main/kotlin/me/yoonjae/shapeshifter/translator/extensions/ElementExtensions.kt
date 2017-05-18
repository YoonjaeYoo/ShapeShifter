package me.yoonjae.shapeshifter.translator.extensions

import org.w3c.dom.Element

val Element.layoutType: String
    get() {
        return when (tagName) {
            "LinearLayout" -> "LinearLayout"
            "TextView" -> "TextView"
            "Button" -> "Button"
            else -> "FrameLayout"
        }
    }

val Element.viewType: String
    get() {
        return when (tagName) {
            "ImageView" -> "UIImageView"
            "TextView" -> "UILabel"
            "Button" -> "UIButton"
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
    return attr("style")?.substring(7)
}

fun Element.margin(): Map<String, String> {
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
        params.clear()
    }
    return params
}

fun Element.padding(): Map<String, String> {
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
        params.clear()
    }
    return params
}

fun Element.verticalAlignment(): String {
    return when (getAttribute("android:layout_height")) {
        "match_parent" -> ".fill"
        else -> {
            val gravity = getAttribute("android:layout_gravity").split("|")
            if (gravity.contains("top")) {
                ".top"
            } else if (gravity.contains("center") || gravity.contains("center_vertical")) {
                ".center"
            } else if (gravity.contains("bottom")) {
                ".bottom"
            } else {
                ".top"
            }
        }
    }
}

fun Element.horizontalAlignment(): String {
    return when (getAttribute("android:layout_width")) {
        "match_parent" -> ".fill"
        else -> {
            val gravity = getAttribute("android:layout_gravity").split("|")
            if (gravity.contains("left") || gravity.contains("start")) {
                ".leading"
            } else if (gravity.contains("center") || gravity.contains("center_horizontal")) {
                ".center"
            } else if (gravity.contains("right") || gravity.contains("end")) {
                ".trailing"
            } else {
                ".leading"
            }
        }
    }
}

fun Element.width(): String {
    val width = getAttribute("android:layout_width")
    return when (width) {
        "match_parent" -> "MATCH_PARENT"
        "wrap_content" -> "WRAP_CONTENT"
        else -> width.toDimen()
    }
}

fun Element.height(): String {
    val height = getAttribute("android:layout_height")
    return when (height) {
        "match_parent" -> "MATCH_PARENT"
        "wrap_content" -> "WRAP_CONTENT"
        else -> height.toDimen()
    }
}

fun Element.image(): String? {
    return attr("android:src")?.let {
        if (it.startsWith("@drawable/")) "UIImage(named: \"${it.substring(10)}\")" else null
    }
}

fun Element.text(): String {
    return attr("android:text")?.let {
        if (it.startsWith("@string/")) {
            "\"${it.substring(8)}\".localized()"
        } else {
            "\"$it\""
        }
    } ?: "\"\""
}

fun Element.alpha(): String? {
    return attr("android:alpha")
}

fun Element.backgroundColor(): String? {
    return attr("android:background")?.let {
        if (it.startsWith("@color/")) "Color.${it.substring(7).toResourceName()}" else null
    }
}
