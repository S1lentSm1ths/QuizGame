package com.vsc.quizgame.view.api

data class Result(
    val response_code: Int,
    val results: List<Result>
)