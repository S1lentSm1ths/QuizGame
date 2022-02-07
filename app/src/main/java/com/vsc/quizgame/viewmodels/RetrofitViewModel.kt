package com.vsc.quizgame.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsc.quizgame.view.api.ApiRepository
import com.vsc.quizgame.view.api.Question
import kotlinx.coroutines.launch

class RetrofitViewModel : ViewModel() {

    private val repository = ApiRepository()
    private val _questionLiveData = MutableLiveData<Question>()
    val questionLiveData: LiveData<Question> = _questionLiveData

    fun refreshQuestion(){
        viewModelScope.launch {
            val response = repository.getQuestion()
            _questionLiveData.postValue(response)

        }
    }
}