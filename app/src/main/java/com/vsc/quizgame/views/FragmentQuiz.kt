package com.vsc.quizgame.view

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.vsc.quizgame.R
import com.vsc.quizgame.view.api.Question
import com.vsc.quizgame.view.api.QuestionApi
import com.vsc.quizgame.view.viewmodels.RetrofitViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.random
import java.util.*


class FragmentQuiz : Fragment() {

    val viewModel: RetrofitViewModel by lazy {
        ViewModelProvider(this).get(RetrofitViewModel::class.java)
    }
    private lateinit var radioButtonFirstAnswer: AppCompatRadioButton
    private lateinit var radioButtonSecondAnswer: AppCompatRadioButton
    private lateinit var radioButtonThirdAnswer: AppCompatRadioButton
    private lateinit var radioButtonFourthAnswer: AppCompatRadioButton
    private lateinit var btnSubmitAnswer: Button
    private lateinit var btnNextQuestion: MaterialButton
    private lateinit var questionText: TextView
    private var correctAnswer: String = ""
    var serialCorrectAnswers: Int = 0
    private var isYourAnswerCorrect: Boolean = false
    private var isButtonSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        initViews(view)
        getNewQuestion()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRadioButtonsClicked()
        onSubmitButtonClicked()
    }

    private fun initViews(view: View) {
        radioButtonFirstAnswer = view.findViewById(R.id.first_answer_button)
        radioButtonSecondAnswer = view.findViewById(R.id.second_answer_button)
        radioButtonThirdAnswer = view.findViewById(R.id.third_answer_button)
        radioButtonFourthAnswer = view.findViewById(R.id.fourth_answer_button)
        btnSubmitAnswer = view.findViewById(R.id.btn_submit_answer)
        btnNextQuestion = view.findViewById(R.id.btn_next_question)
        questionText = view.findViewById(R.id.example_question)
    }

    // TODO: 3.2.2022 г. Тази функция трябва да бъде поставена под getNewQuestion()
    private fun onRadioButtonsClicked() {
//        val buttonCheckedTextColor =
//            ContextCompat.getColor(requireContext(), R.color.checked_buttons_textColor)
//        val buttonUnCheckedTextColor =
//            ContextCompat.getColor(requireContext(), R.color.unchecked_buttons_textColor)
        var yourAnswer = ""
        val btnSubmitActiveColor = ContextCompat.getColor(requireContext(), R.color.white)

        radioButtonFirstAnswer.setOnClickListener() {
            if (radioButtonFirstAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                yourAnswer = radioButtonFirstAnswer.text.toString()
                isButtonSelected = true
            }
        }

        radioButtonSecondAnswer.setOnClickListener() {
            if (radioButtonSecondAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                yourAnswer = radioButtonSecondAnswer.text.toString()
                isButtonSelected = true
            }
        }

        radioButtonThirdAnswer.setOnClickListener() {
            if (radioButtonThirdAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                yourAnswer = radioButtonThirdAnswer.text.toString()
                isButtonSelected = true
            }
        }

        radioButtonFourthAnswer.setOnClickListener() {
            if (radioButtonFourthAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                yourAnswer = radioButtonFourthAnswer.text.toString()
                isButtonSelected = true
            }
        }

        isYourAnswerCorrect = yourAnswer.contains(correctAnswer)
    }

    private fun getNewQuestion() {

        viewModel.refreshQuestion()
        viewModel.questionLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(this.context, "Unsuccessful api call!", Toast.LENGTH_LONG) .show()
                return@observe
            } else {

//            for (i in 0..9) {

                // Setting answers to random positions/buttons
                when (val randomNumber = (0..3).random()) {
                    0 -> {
                        radioButtonFirstAnswer.text = response.correct_answer
//                        radioButtonSecondAnswer.text = response.incorrect_answers[1]
//                        radioButtonThirdAnswer.text =
//                            response.incorrect_answers[1]
//                        radioButtonFourthAnswer.text =
//                            response.incorrect_answers[2]
                    }
                    1 -> {
//                        radioButtonFirstAnswer.text =
//                            response.incorrect_answers[1]
                        radioButtonSecondAnswer.text = response.correct_answer
//                        radioButtonThirdAnswer.text = response.incorrect_answers[1]
//                        radioButtonFourthAnswer.text =
//                            response.incorrect_answers[2]
                    }
                    2 -> {
//                        radioButtonFirstAnswer.text =
//                            response.incorrect_answers[1]
//                        radioButtonSecondAnswer.text =
//                            response.incorrect_answers[1]
                        radioButtonThirdAnswer.text = response.correct_answer
//                        radioButtonFourthAnswer.text = response.incorrect_answers[2]
                    }
                    3 -> {
//                        radioButtonFirstAnswer.text =
//                            response.incorrect_answers[1]
//                        radioButtonSecondAnswer.text =
//                            response.incorrect_answers[1]
//                        radioButtonThirdAnswer.text =
//                            response.incorrect_answers[2]
                        radioButtonFourthAnswer.text = response.correct_answer
                    }
                } // The end of the condition
            }
            questionText.text = response.question
            correctAnswer = response.correct_answer
        }
    }

    private fun onSubmitButtonClicked() {
        btnSubmitAnswer.setOnClickListener() {

            if (isButtonSelected) {
                if (isYourAnswerCorrect) {
                    Toast.makeText(this.context, "Correct answer!", Toast.LENGTH_LONG).show()
                    serialCorrectAnswers += 1
                } else {
                    Toast.makeText(
                        this.context,
                        "Wrong! The correct answer is ".plus(correctAnswer),
                        Toast.LENGTH_LONG
                    ).show()
                    serialCorrectAnswers = 0
                }

                btnNextQuestion.visibility = View.VISIBLE
                btnSubmitAnswer.visibility = View.GONE
//
                radioButtonFirstAnswer.isEnabled = false
                radioButtonSecondAnswer.isEnabled = false
                radioButtonThirdAnswer.isEnabled = false
                radioButtonFourthAnswer.isEnabled = false
            }
            else {
                Toast.makeText(this.context, "Please select answer!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
//}
