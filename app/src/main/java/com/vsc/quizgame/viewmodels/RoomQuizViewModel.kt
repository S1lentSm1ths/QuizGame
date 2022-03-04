package com.vsc.quizgame.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.vsc.quizgame.data.AppDataBase
import com.vsc.quizgame.data.QuizPoints
import kotlinx.coroutines.launch

class RoomQuizViewModel(app: Application) : AndroidViewModel(app) {

    private val quizDao = AppDataBase.getInstance(getApplication()).QuizDao()
    private val _quizStatsLiveData = MutableLiveData<List<QuizPoints>>()
    val quizStatsLiveData: LiveData<List<QuizPoints>> = _quizStatsLiveData

    fun getStatsData() {
        viewModelScope.launch {
            val data = quizDao.getStats()
            _quizStatsLiveData.postValue(data!!)
        }
    }

    fun insertStat(stat: QuizPoints) {
        viewModelScope.launch {
            quizDao.insertNewStat(stat)
        }
    }
}