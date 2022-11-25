package com.erickpimentel.mercadoeditorial.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.Book
import com.erickpimentel.mercadoeditorial.utils.Status
import com.erickpimentel.mercadoeditorial.utils.Type
import retrofit2.HttpException

class BooksPagingSource(
    private val repository: ApiRepository,
    private val currentQuery: String?,
    private val type: Type?,
    private val status: Status?
): PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            var title: String? = null
            var isbn: String? = null
            currentQuery?.let {
                if (isNumeric(it)) isbn = it
                else title = it
            }

            val currentPage = params.key ?: 1
            val response = repository.getBooks(
                currentPage,
                type?.name,
                status?.code ?: 1,
                title,
                isbn,
                null,
                null)
            val data = response.body()!!.books
            val responseData = mutableListOf<Book>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }catch (httpE: HttpException){
            LoadResult.Error(httpE)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return null
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}