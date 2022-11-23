package com.erickpimentel.mercadoeditorial.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erickpimentel.mercadoeditorial.repository.ApiRepository
import com.erickpimentel.mercadoeditorial.response.Book
import retrofit2.HttpException

class BooksPagingSource(private val repository: ApiRepository): PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getBooks(currentPage, 1, null, null)
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
}