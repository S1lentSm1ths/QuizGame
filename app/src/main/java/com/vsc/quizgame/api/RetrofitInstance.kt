package com.vsc.quizgame.view.api

//import com.vsc.quizgame.view.Constants
import com.vsc.quizgame.api.Api
import com.vsc.quizgame.view.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val quotesApi: QuestionApi by lazy {
        retrofit.create(QuestionApi::class.java)
    }

    val api = Api(quotesApi)
}