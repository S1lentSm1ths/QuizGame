package com.vsc.quizgame.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vsc.quizgame.R
import com.vsc.quizgame.data.QuizPoints
import com.vsc.quizgame.databinding.ScoreBoardModelBinding

class ScoreboardAdapter : RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder>() {

    private var statsList = mutableListOf<QuizPoints>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.score_board_model, parent, false)

        return ScoreboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreboardViewHolder, position: Int) {
        val stat = statsList[position]

        holder.onBind(stat)
    }

    override fun getItemCount() = statsList.size

    fun setStatsToRecycler(stats: List<QuizPoints>){
        statsList.clear()
        statsList.addAll(stats)
        notifyDataSetChanged()
    }

    class ScoreboardViewHolder(quizStats: View) : RecyclerView.ViewHolder(quizStats) {

        private val quizStatDate: TextView = quizStats.findViewById(R.id.date_time_data)
        private val quizStatPoints: TextView = quizStats.findViewById(R.id.stats_data)

        fun onBind(stat: QuizPoints) {
            quizStatDate.text = stat.dateTime.toString()
            quizStatPoints.text = stat.points.toString()
        }
    }
}

