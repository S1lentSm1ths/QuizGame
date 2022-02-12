package com.vsc.quizgame.api

import com.vsc.quizgame.view.api.QuestionApi

class Api (private val questionApi: QuestionApi){

    fun getQuestion(): QuizResponse?{
        return questionApi.getQuestion().execute().body()
    }
}