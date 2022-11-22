package com.erickpimentel.mercadoeditorial.response

data class Navigation(
    val next_page: Int,
    val page: Int,
    val prev_page: Int,
    val total_pages: Int,
    val total_records: Int
)