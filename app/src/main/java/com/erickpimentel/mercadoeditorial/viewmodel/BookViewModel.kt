package com.erickpimentel.mercadoeditorial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erickpimentel.mercadoeditorial.paging.BooksPagingSource
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.utils.Status
import com.erickpimentel.mercadoeditorial.utils.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: ApiRepository): ViewModel() {

    private val _currentBook = MutableLiveData<Book>()
    val currentBook: LiveData<Book> get() = _currentBook

    private val _currentQuery = MutableLiveData<String?>()
    val currentQuery: LiveData<String?> get() = _currentQuery

    private val _suggestionsList = arrayListOf<String>()
    val suggestionsList: ArrayList<String> = _suggestionsList

    private val _type = MutableLiveData<Type?>()
    val type: LiveData<Type?> get() = _type

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> get() = _status

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

    fun clearAllRadioButtons(){
        _type.value = null
        _status.value = null
    }

    private fun insertType(type: Type?){
        _type.value = type
    }

    fun updateType(type: Type?){
        insertType(type)
    }

    private fun insertStatus(status: Status?){
        _status.value = status
    }

    fun updateStatus(status: Status?){
        insertStatus(status)
    }

    val bookList = getSearchResultStream(currentQuery.value, type.value, status.value).cachedIn(viewModelScope)

    fun getSearchResultStream(query: String?, type: Type?, status: Status?): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(1),
            pagingSourceFactory = { BooksPagingSource(repository, query, type, status) }
        ).flow
    }
}