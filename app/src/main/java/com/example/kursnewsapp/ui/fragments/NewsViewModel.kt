package com.example.kursnewsapp.ui.fragments

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kursnewsapp.models.Article
import com.example.kursnewsapp.models.NewsResponse
import com.example.kursnewsapp.repository.NewsRepository
import com.example.kursnewsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import java.util.Locale.IsoCountryCode

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    var headlinesPage = 1
    var headlinesResponse: NewsResponse? = null

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> get() = _snackbarMessage

    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getHeadlines("us")
    }

    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        fetchHeadlinesWithInternetCheck(countryCode)
    }

    private fun handleHeadlineResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++

                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles?.toMutableList() ?: mutableListOf()
                    val newArticles = resultResponse.articles

                    oldArticles.addAll(newArticles)

                    headlinesResponse = headlinesResponse?.copy(articles = oldArticles)
                }

                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    fun addToFavorites(article: Article) = viewModelScope.launch {
//        newsRepository.insertArticle(article)
//        _snackbarMessage.postValue("Added to favorites")
//    }
//
//    fun delete(article:Article)=viewModelScope.launch {
//        newsRepository.deleteArticle(article)
//        _snackbarMessage.postValue("Removed from favorites")
//    }

    fun toggleFavorite(article: Article) = viewModelScope.launch {
        val isFavorite = newsRepository.isArticleFavorite(article.key ?: -1)
        System.out.println(article)
        System.out.println(isFavorite)
        if (isFavorite) {
            newsRepository.deleteArticle(article)
            _snackbarMessage.postValue("Removed from favorites")
        } else {
            newsRepository.insertArticle(article)
            _snackbarMessage.postValue("Added to favorites")
        }
    }

//    fun deleteOrAddToFavorites(article: Article) = viewModelScope.launch {
//        try {
//            newsRepository.deleteArticle(article)
//            _snackbarMessage.postValue("Removed from favorites")
//        } catch (e: Exception) {
//            newsRepository.insertArticle(article)
//            _snackbarMessage.postValue("Added to favorites")
//        }
//    }

    fun getAll() = newsRepository.getAllArticle()

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

    private suspend fun fetchHeadlinesWithInternetCheck(countryCode: String) {
        headlines.postValue(Resource.Loading())
        try {
            if (isInternetAvailable(getApplication())) {
                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleHeadlineResponse(response))
            } else {
                headlines.postValue(Resource.Error("No Internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resource.Error("Unable to connect"))
                else -> headlines.postValue(Resource.Error("No signal"))
            }
        }
    }
}
