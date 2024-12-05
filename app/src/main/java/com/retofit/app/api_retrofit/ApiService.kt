package com.retofit.app.api_retrofit


import com.retofit.app.data_model_class.JsonDataNewList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("everything")
    suspend fun getDataList(
        @Query("q") nameString: String,
        @Query("from") dateString: String,
        @Query("sortBy") sortByString: String,
        @Query("apiKey") apiKey: String
    ): Response<JsonDataNewList>


}