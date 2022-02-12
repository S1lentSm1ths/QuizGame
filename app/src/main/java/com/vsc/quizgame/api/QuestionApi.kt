package com.vsc.quizgame.view.api

import com.vsc.quizgame.api.QuizResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface QuestionApi {
    @GET("api.php?amount=10&category=22&type=multiple")
    fun getQuestion(): Call<QuizResponse>
}