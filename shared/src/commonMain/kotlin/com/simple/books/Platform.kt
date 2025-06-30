package com.simple.books

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
