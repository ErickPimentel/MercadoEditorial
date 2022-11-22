package com.erickpimentel.mercadoeditorial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickpimentel.mercadoeditorial.response.Book

class BookViewModel: ViewModel() {

    private val _currentBook = MutableLiveData<Book>()
    val currentBook: MutableLiveData<Book> = _currentBook

    private val _currentQuery = MutableLiveData<String?>()
    val currentQuery: MutableLiveData<String?> = _currentQuery

    private fun insertCurrentBook(book: Book){
        _currentBook.value = book
    }

    fun addCurrentBook(book: Book){
        insertCurrentBook(book)
    }

    private fun insertCurrentQuery(query: String?){
        _currentQuery.value = query
    }

    fun updateQuery(query: String?){
        insertCurrentQuery(query)
    }

}