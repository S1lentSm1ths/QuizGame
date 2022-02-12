package com.vsc.quizgame.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vsc.quizgame.views.QuizFragment
import com.vsc.quizgame.view.FragmentScoreboard

internal class ViewPagerAdapter(
    var context: Context,
    fragmentManager: FragmentManager,
    var tabsCount: Int
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                QuizFragment()
            }

            1 -> {
                FragmentScoreboard()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return tabsCount
    }
}