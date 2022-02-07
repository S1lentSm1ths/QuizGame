package com.vsc.quizgame.view.api

//import com.vsc.quizgame.view.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val questionApi: QuestionApi by lazy{
        retrofit.create(QuestionApi::class.java)
    }

    val api = Api(questionApi)
}