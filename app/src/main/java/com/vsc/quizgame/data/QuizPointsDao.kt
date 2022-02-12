package com.vsc.quizgame.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizPointsDao {
    @Query("SELECT * FROM quiz_db ORDER BY serialCorrectAnswersPoints DESC LIMIT 10")
    suspend fun getStats(): List<QuizPoints>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewStat(stat: QuizPoints?)
}