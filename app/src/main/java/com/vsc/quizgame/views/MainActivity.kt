package com.vsc.quizgame.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vsc.quizgame.R
import com.vsc.quizgame.api.NetworkConnection
import com.vsc.quizgame.databinding.ActivityMainBinding
//import com.vsc.quizgame.view.Constants.Companion.BASE_URL
import com.vsc.quizgame.view.adapters.ViewPagerAdapter
import com.vsc.quizgame.views.QuizFragment

class MainActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var correctAnswersCounter: TextView
    private val fragmentQuiz = QuizFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        correctAnswersCounter = binding.correctAnswersCounter
        correctAnswersCounter.text = fragmentQuiz.serialCorrectAnswers.toString()
        setToolBarTitle()
        initTabLayout()
    }

    private fun initViews(){
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
    }

    private fun setToolBarTitle() {
        toolbar = binding.toolbarMenu
        toolbar.title = "Quiz Game"
    }

    private fun initTabLayout() {

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