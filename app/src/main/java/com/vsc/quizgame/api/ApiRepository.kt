package com.vsc.quizgame.view.api

import androidx.compose.ui.input.key.Key.Companion.T

class ApiRepository {

    val answersList = ArrayList<String>()
    val question = Question(
        "",
        "",
        "",
        answersList,
        "No question",
        ""
    )

    suspend fun getQuestion(): Question {
        val request = RetrofitInstance.api.getQuestion()

        return when {
            request.isSuccessful -> {
                request.body()!!
            }
            else -> {
                question
            }
        }
    }
}