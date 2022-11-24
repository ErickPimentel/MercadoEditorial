package com.erickpimentel.mercadoeditorial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.erickpimentel.mercadoeditorial.paging.BooksPagingSource
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: ApiRepository): ViewModel() {

    private val _currentBook = MutableLiveData<Book>()
    val currentBook: LiveData<Book> get() = _currentBook

    private val _currentQuery = MutableLiveData<String?>()
    val currentQuery: LiveData<String?> get() = _currentQuery

    private val _suggestionsList = arrayListOf<String>()
    val suggestionsList: ArrayList<String> = _suggestionsList

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

    private fun insertSuggestion(suggestion: String){
        _suggestionsList.add(suggestion)
    }

    fun addSuggestion(suggestion: String){
        insertSuggestion(suggestion)
    }

    val bookList = Pager(PagingConfig(1)){
        BooksPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

}