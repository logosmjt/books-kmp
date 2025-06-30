package com.simple.books.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

data class Tag(val name: String, val href: String? = null, val start: Int)

/**
 * Parses a limited subset of HTML to an AnnotatedString for Compose.
 * Supports <b>, <i>, <u>, <p>, <br>, and <a href="...">.
 */
actual fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var index = 0
    val tagRegex = Regex("""<(/)?([a-z]+)([^>]*)>""", RegexOption.IGNORE_CASE)
    val hrefRegex = Regex("""href\s*=\s*['"]([^'"]+)['"]""", RegexOption.IGNORE_CASE)

    val tagStack = mutableListOf<Tag>()

    while (index < html.length) {
        val match = tagRegex.find(html, index)

        if (match == null) {
            // Add remaining text
            val remaining = html.substring(index)
            appendStyledText(builder, remaining, tagStack)
            break
        }

        // Text before the tag
        val textBefore = html.substring(index, match.range.first)
        appendStyledText(builder, textBefore, tagStack)

        val isClosing = match.groupValues.getOrNull(1) == "/"
        val tagName = match.groupValues.getOrNull(2)?.lowercase() ?: ""
        val attrs = match.groupValues.getOrNull(3) ?: ""

        when {
            tagName == "br" && !isClosing -> builder.append("\n")
            tagName == "p" && !isClosing -> {
                if (builder.length > 0) builder.append("\n\n")
                tagStack.add(Tag(tagName, null, builder.length))
            }
            isClosing -> tagStack.removeLastOrNull { it.name == tagName }
            tagName == "a" -> {
                val href = hrefRegex.find(attrs)?.groupValues?.getOrNull(1)
                tagStack.add(Tag("a", href, builder.length))
            }
            tagName in listOf("b", "i", "u") -> {
                tagStack.add(Tag(tagName, null, builder.length))
            }
        }

        index = match.range.last + 1
    }

    return builder.toAnnotatedString()
}

private fun appendStyledText(
    builder: AnnotatedString.Builder,
    text: String,
    tagStack: List<Tag>
) {
    if (text.isBlank()) return

    val style = getCurrentStyle(tagStack.map { it.name })
    val href = tagStack.lastOrNull { it.name == "a" }?.href
    val start = builder.length

    builder.withStyle(style) {
        append(text)
    }

    if (href != null) {
        builder.addStringAnnotation(
            tag = "URL",
            annotation = href,
            start = start,
            end = builder.length
        )
    }
}

private fun getCurrentStyle(tags: List<String>): SpanStyle {
    return SpanStyle(
        fontWeight = if ("b" in tags) FontWeight.Bold else null,
        fontStyle = if ("i" in tags) FontStyle.Italic else null,
        textDecoration = when {
            "u" in tags || "a" in tags -> TextDecoration.Underline
            else -> null
        },
        color = if ("a" in tags) Color(0xFF0645AD) else Color.Unspecified
    )
}

private inline fun <T> MutableList<T>.removeLastOrNull(predicate: (T) -> Boolean) {
    val index = indexOfLast(predicate)
    if (index >= 0) removeAt(index)
}
