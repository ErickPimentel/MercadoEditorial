package com.erickpimentel.mercadoeditorial.api

import com.erickpimentel.mercadoeditorial.response.BookListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("book")
    fun getBooks(
        @Query("codigo_status") statusCode : Int?,
        @Query("titulo") title : String?,
        @Query("isbn") isbn : String?
    ) : Call<BookListResponse>

}