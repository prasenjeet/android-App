package com.example.zfold5app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.zfold5app.model.Article
import com.example.zfold5app.model.sampleArticles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    val articles: List<Article> = sampleArticles

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    private val _showDetail = MutableStateFlow(false)
    val showDetail: StateFlow<Boolean> = _showDetail.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
        _showDetail.value = true
    }

    fun clearSelection() {
        _selectedArticle.value = null
        _showDetail.value = false
    }
}
