package com.vsc.quizgame.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "quiz_db")
data class QuizPoints(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val statsId: Long? = null,
    @ColumnInfo(name = "serialCorrectAnswersPoints") val points: Int,
    @ColumnInfo(name = "dateTime") val dateTime: String?

)
