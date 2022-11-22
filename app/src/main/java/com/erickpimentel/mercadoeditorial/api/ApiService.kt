package com.erickpimentel.mercadoeditorial.api

import com.erickpimentel.mercadoeditorial.response.BookListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("book")
    fun getBooksByStatusCode(@Query("codigo_status") statusCode : Int) : Call<BookListResponse>

    @GET("book")
    fun getBookByTitle(@Query("titulo") title : String) : Call<BookListResponse>

}