package com.vsc.quizgame.view.api

import com.vsc.quizgame.api.QuizResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApi {
    @GET("api.php?amount=10&type=multiple")
    fun getQuestion(@Query("category") category: Int): Call<QuizResponse>
}