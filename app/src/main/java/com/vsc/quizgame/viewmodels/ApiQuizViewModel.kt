package com.vsc.quizgame.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vsc.quizgame.view.api.Question
import com.vsc.quizgame.view.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiQuizViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val _questionLiveData = MutableLiveData<List<Question>?>()
    val questionLiveData: LiveData<List<Question>?> = _questionLiveData

    fun refreshQuestion(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.getQuestion()?.results

            _questionLiveData.postValue(response)

        }
    }
}