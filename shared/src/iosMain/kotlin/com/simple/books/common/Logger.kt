package com.simple.books.common

import platform.Foundation.NSLog

actual object Logger {
    actual fun d(tag: String, message: String) {
        NSLog("$tag: $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        NSLog("$tag: $message\nError: ${throwable?.message}")
    }
}
