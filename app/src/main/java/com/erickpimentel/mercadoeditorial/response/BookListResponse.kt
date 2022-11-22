package com.erickpimentel.mercadoeditorial.response

data class BookListResponse(
    val books: List<Book>,
    val navigation: Navigation,
    val status: Status
)