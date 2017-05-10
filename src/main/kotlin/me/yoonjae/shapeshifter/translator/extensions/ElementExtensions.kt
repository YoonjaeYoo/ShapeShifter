package me.yoonjae.shapeshifter.translator.extensions

import org.w3c.dom.Element

fun Element.id(): String? {
    return if (hasAttribute("android:id")) getAttribute("android:id").substring(5) else null
}

fun Element.style(): String? {
    return if (hasAttribute("style")) getAttribute("style").substring(7) else null
}

fun Element.insets(): Map<String, String> {
    val params = mutableMapOf("top" to "0", "left" to "0", "bottom" to "0", "right" to "0")
    params.keys.forEach { key ->
        getAttribute("android:layout_margin${key.capitalize()}").let {
            if (it.isNotEmpty()) params[key] = it.toDimen()
        }
    }
    getAttribute("android:layout_margin").let {
        if (it.isNotEmpty()) {
            params.keys.forEach { key ->
                params[key] = it.toDimen()
            }
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
        "match_parent" -> "nil"
        "wrap_content" -> "nil"
        else -> width.toDimen()
    }
}

fun Element.height(): String {
    val height = getAttribute("android:layout_height")
    return when (height) {
        "match_parent" -> "nil"
        "wrap_content" -> "nil"
        else -> height.toDimen()
    }
}

fun Element.image(): String? {
    val src = getAttribute("android:src")
    return if (src.isNotEmpty() && src.startsWith("@drawable/")) {
        "UIImage(named: \"${src.substring(10)}\")"
    } else {
        null
    }
}

fun Element.text(): String? {
    val text = getAttribute("android:text")
    return if (text.isNotEmpty()) {
        if (text.startsWith("@string/")) {
            "\"${text.substring(8)}\".localized()"
        } else {
            "\"$text\""
        }
    } else {
        null
    }
}
