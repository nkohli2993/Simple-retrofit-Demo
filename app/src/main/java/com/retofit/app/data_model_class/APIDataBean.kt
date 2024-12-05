package com.retofit.app.data_model_class

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JsonDataNewList(

    @SerializedName("status") var status: String? = null,
    @SerializedName("totalResults") var totalResults: Int? = null,
    @SerializedName("articles") var articles: ArrayList<Articles> = arrayListOf()

) : Parcelable



@Parcelize
data class Source(

    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null

) : Parcelable


@Parcelize
data class Articles(
    @SerializedName("index") var index: Int? = null,
    @SerializedName("source") var source: Source? = Source(),
    @SerializedName("author") var author: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("urlToImage") var urlToImage: String? = null,
    @SerializedName("publishedAt") var publishedAt: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("is_bookmarked") var isBookmarked: Boolean = false

):Parcelable