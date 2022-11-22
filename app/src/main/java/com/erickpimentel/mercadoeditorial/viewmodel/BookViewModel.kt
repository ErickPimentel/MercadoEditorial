package com.erickpimentel.mercadoeditorial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickpimentel.mercadoeditorial.response.Book

class BookViewModel: ViewModel() {

    private val _currentBook = MutableLiveData<Book>()
    val currentBook: MutableLiveData<Book> = _currentBook

    private fun insertCurrentBook(book: Book){
        _currentBook.value = book
    }

    fun addCurrentBook(book: Book){
        insertCurrentBook(book)
    }

}