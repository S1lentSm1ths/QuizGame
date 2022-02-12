package com.vsc.quizgame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vsc.quizgame.R
import com.vsc.quizgame.adapters.ScoreboardAdapter
import com.vsc.quizgame.databinding.FragmentScoreboardBinding
import com.vsc.quizgame.viewmodels.RoomQuizViewModel

class FragmentScoreboard : Fragment() {

    private lateinit var binding: FragmentScoreboardBinding
    private val viewModel: RoomQuizViewModel by lazy {
        ViewModelProvider(this)[RoomQuizViewModel::class.java]
    }
    private lateinit var recyclerView: RecyclerView
    private val adapter = ScoreboardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScoreboardBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getQuizStatsData()
    }

    private fun initRecyclerView() {

        recyclerView = binding.quizStatsRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getQuizStatsData() {
        viewModel.getStatsData()
        viewModel.quizStatsLiveData.observe(this) {
            if (it == null) {
                error("Can not access data")
            } else {
                Toast.makeText(this.context, "You have data", Toast.LENGTH_LONG).show()
                adapter.setStatsToRecycler(it)
            }
        }
    }
}