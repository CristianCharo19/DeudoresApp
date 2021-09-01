package com.davidcharo.deudoresapp.utils

import androidx.core.util.PatternsCompat

fun validateEmail(text: String): Boolean {
    return (PatternsCompat.EMAIL_ADDRESS.matcher(text).matches())
}

fun validatePassword(text: String, minTextSize: Int): Boolean {
    val textSize = text.length
    return textSize >= minTextSize
}