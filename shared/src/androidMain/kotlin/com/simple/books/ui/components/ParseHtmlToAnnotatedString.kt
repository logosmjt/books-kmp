package com.simple.books.ui.components

import android.text.Spanned
import androidx.compose.ui.text.AnnotatedString
import androidx.core.text.HtmlCompat

actual fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
    val spanned: Spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    return AnnotatedString(spanned.toString())
}
