package com.retofit.app.network.retrofit

import android.util.Log
import androidx.annotation.MainThread
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

abstract class NetworkOnlineDataRepo<RESULT, REQUEST> {
    fun asFlow() = flow {
        emit(DataResult.Loading())
        try {
            val apiResponse = fetchDataFromRemoteSource()
            val data = apiResponse.body()

            if (apiResponse.isSuccessful && data != null) {
                emit(DataResult.Success(data))
            } else {

                emit(
                    DataResult.Failure(
                        getErrorMsg(apiResponse.errorBody()!!),
                        errorCode = apiResponse.code()
                    )
                )

            }
        } catch (e: Exception) {
            emit(
                DataResult.Failure(
                    e.message, e
                )
            )
        }

    }

    fun getErrorMsg(responseBody: ResponseBody): String {

        try {
            val jsonObject = JSONObject(responseBody.string())

            return jsonObject.getString("message")

        } catch (e: Exception) {
            return e.message!!
        }

    }

    fun getErrorMsg(responseBody: REQUEST): String {

        try {
            val jsonObject = JSONObject(Gson().toJson(responseBody))

            return jsonObject.getString("message")

        } catch (e: Exception) {
            return e.message!!
        }

    }

    @MainThread
    protected abstract suspend fun fetchDataFromRemoteSource(): Response<REQUEST>
}