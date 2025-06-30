package com.simple.books

import app.cash.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver


fun fakeErrorResponse(): io.ktor.client.statement.HttpResponse {
    throw IllegalStateException("Mock Http error.")
}

fun fakeError(message: String = "Mock repo error."): RuntimeException {
    throw IllegalStateException(message)
}
