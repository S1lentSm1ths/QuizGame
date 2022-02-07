package com.vsc.quizgame.view.adapters

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vsc.quizgame.view.FragmentQuiz
import com.vsc.quizgame.view.FragmentScoreboard
import androidx.fragment.app.FragmentStatePagerAdapter as FragmentStatePagerAdapter1

internal class ViewPagerAdapter(
    var context: Context,
    fragmentManager: FragmentManager,
    var tabsCount: Int
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                FragmentQuiz()
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