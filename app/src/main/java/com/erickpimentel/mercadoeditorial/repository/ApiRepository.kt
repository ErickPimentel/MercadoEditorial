package com.erickpimentel.mercadoeditorial.repository

import com.erickpimentel.mercadoeditorial.api.ApiServices
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun getBooks(page: Int?, statusCode: Int?, title: String?, isbn: String?) = apiServices.getBooks(page, statusCode, title, isbn)
}