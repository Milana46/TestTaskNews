package com.example.kursnewsapp.ui.fragments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kursnewsapp.repository.NewsRepository
import kotlin.reflect.KClass


class NewsViewModelProviderFactory(val app:Application, private val newsRepository: NewsRepository)
    :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }
}
