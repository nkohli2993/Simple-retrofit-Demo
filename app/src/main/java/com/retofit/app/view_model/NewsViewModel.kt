package com.retofit.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.retofit.app.base.BaseViewModel
import com.retofit.app.data_model_class.JsonDataNewList
import com.retofit.app.network.retrofit.DataResult
import com.retofit.app.network.retrofit.Event
import com.retofit.app.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NewsViewModel  @Inject constructor(
    private val newsRepository: NewsRepository,
) : BaseViewModel() {

    private var _newsResponseLiveData =
        MutableLiveData<Event<DataResult<JsonDataNewList>>>()


    var newsResponseLiveData: LiveData<Event<DataResult<JsonDataNewList>>> =
        _newsResponseLiveData


    fun getNewsList(  nameString: String, dateString: String, sortByString: String,
                       apiKey: String) {
        viewModelScope.launch {
            val response =
                newsRepository.getListNews(nameString,dateString,sortByString,apiKey)
            withContext(Dispatchers.Main) {
                response.collect { _newsResponseLiveData.postValue(Event(it)) }
            }
        }
    }
  }