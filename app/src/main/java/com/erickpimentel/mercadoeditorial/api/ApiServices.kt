package com.erickpimentel.mercadoeditorial.api

import com.erickpimentel.mercadoeditorial.response.BookListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("book")
    suspend fun getBooks(
        @Query("page") page : Int?,
        @Query("codigo_status") statusCode : Int?,
        @Query("titulo") title : String?,
        @Query("isbn") isbn : String?
    ) : Response<BookListResponse>

}