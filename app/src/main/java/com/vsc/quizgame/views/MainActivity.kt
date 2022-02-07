package com.vsc.quizgame.view

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.textclassifier.TextLinks
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.vsc.quizgame.R
//import com.vsc.quizgame.view.Constants.Companion.BASE_URL
import com.vsc.quizgame.view.adapters.ViewPagerAdapter
import com.vsc.quizgame.view.api.Question
import com.vsc.quizgame.view.api.QuestionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener as OnTabSelectedListener1

class MainActivity() : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var questionText: TextView
    private lateinit var correctAnswersCounter: TextView
    private val fragmentQuiz = FragmentQuiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        correctAnswersCounter = findViewById(R.id.correct_answers_counter)
        correctAnswersCounter.text = fragmentQuiz.serialCorrectAnswers.toString()
        setToolBarTitle()
        initTabLayout()
        //getCurrentQuestion()
    }

    private fun setToolBarTitle() {
        toolbar = findViewById(R.id.toolbar_menu)
        toolbar.title = "Quiz Game"
    }

    private fun initTabLayout() {

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        tabLayout.addTab(tabLayout.newTab().setText("Questions"))
        tabLayout.addTab(tabLayout.newTab().setText("Scoreboard"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter =
            ViewPagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}