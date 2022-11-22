package com.erickpimentel.mercadoeditorial.adapter.listener

import com.erickpimentel.mercadoeditorial.response.Book

interface Listener {
    fun onItemClickListener(book: Book)
}