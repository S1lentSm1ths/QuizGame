package com.vsc.quizgame.api

import com.vsc.quizgame.view.api.QuestionApi

class Api(private val questionApi: QuestionApi) {

    fun getQuestion(category: Int): QuizResponse? {
        return questionApi.getQuestion(category).execute().body()
    }
}