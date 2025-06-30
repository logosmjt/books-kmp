package com.simple.books.ui.components

import androidx.compose.ui.text.AnnotatedString

expect fun parseHtmlToAnnotatedString(html: String): AnnotatedString
