package com.simple.books.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun LinkButton(text: String, url: String) {
    Button(
        onClick = {
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                UIApplication.sharedApplication.openURL(
                    nsUrl,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = null
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Text(text)
    }
}
