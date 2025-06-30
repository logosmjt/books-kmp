package com.simple.books.listing.application

interface QueryUseCase {

    suspend fun invoke(): String?

}
