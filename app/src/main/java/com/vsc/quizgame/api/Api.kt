package com.vsc.quizgame.view.api

//import com.vsc.quizgame.view.Constants.Companion.BASE_URL
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api(private val questionApi: QuestionApi) {

    suspend fun getQuestion(): Response<Question> {
        return questionApi.getQuestion()
    }
}