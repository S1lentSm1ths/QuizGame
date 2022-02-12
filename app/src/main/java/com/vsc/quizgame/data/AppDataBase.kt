package com.vsc.quizgame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vsc.quizgame.view.Constants.Companion.DATABASE


@Database(entities = [QuizPoints::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        private var instance: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as AppDataBase
        }
    }

    abstract fun QuizDao(): QuizPointsDao
}