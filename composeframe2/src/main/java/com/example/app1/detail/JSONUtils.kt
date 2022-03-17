package com.example.app1.detail

import org.json.JSONObject

fun JSONObject.optStringList(key: String): ArrayList<String>? {
    val jsonArray = optJSONArray(key)
    jsonArray?.let {
        val strings = ArrayList<String>()
        for (i in 0 until it.length()) {
            strings.add(it.optString(i))
        }
        return strings
    } ?: let {
        return null
    }
}