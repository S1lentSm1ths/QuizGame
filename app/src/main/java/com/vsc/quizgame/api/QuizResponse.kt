package com.vsc.quizgame.api

import com.vsc.quizgame.view.api.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuizResponse(
    val responseCode: Int,
    val results: List<Question>
)