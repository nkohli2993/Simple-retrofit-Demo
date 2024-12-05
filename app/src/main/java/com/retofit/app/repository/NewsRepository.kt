package com.retofit.app.repository

import com.retofit.app.api_retrofit.ApiService
import com.retofit.app.data_model_class.JsonDataNewList
import com.retofit.app.network.retrofit.DataResult
import com.retofit.app.network.retrofit.NetworkOnlineDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListNews(
        nameString: String, dateString: String, sortByString: String,
        apiKey: String
    ): Flow<DataResult<JsonDataNewList>> {
        return object : NetworkOnlineDataRepo<JsonDataNewList, JsonDataNewList>() {
            override suspend fun fetchDataFromRemoteSource(): Response<JsonDataNewList> {
                return apiService.getDataList(nameString,dateString,sortByString,apiKey)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
}