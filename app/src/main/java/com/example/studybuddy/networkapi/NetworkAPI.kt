package com.example.studybuddy.networkapi

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkAPI {

    @GET("/api/v2/entries/en/hello")
    suspend fun fetchDefinition() : List<JSON>

}