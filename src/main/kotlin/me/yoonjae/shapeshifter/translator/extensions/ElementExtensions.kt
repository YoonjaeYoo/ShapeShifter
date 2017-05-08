package me.yoonjae.shapeshifter.translator.extensions

import org.w3c.dom.Element

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

fun Element.alignment(): Map<String, String> {
    return mapOf("vertical" to verticalAlignment(), "horizontal" to horizontalAlignment())
}

fun Element.verticalAlignment(): String {
    return when (getAttribute("android:layout_height")) {
        "match_parent" -> ".fill"
        "wrap_content" -> ".top"
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
        "wrap_content" -> ".leading"
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
