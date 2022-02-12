package com.vsc.quizgame.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.vsc.quizgame.R
import com.vsc.quizgame.data.QuizPoints
import com.vsc.quizgame.databinding.FragmentQuizBinding
import com.vsc.quizgame.view.viewmodels.ApiQuizViewModel
import com.vsc.quizgame.viewmodels.RoomQuizViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class QuizFragment : Fragment() {

    private val apiQuizViewModel: ApiQuizViewModel by lazy {
        ViewModelProvider(this)[ApiQuizViewModel::class.java]
    }
    private val roomQuizViewModel: RoomQuizViewModel by lazy {
        ViewModelProvider(this)[RoomQuizViewModel::class.java]
    }
    private lateinit var binding: FragmentQuizBinding
    private lateinit var dropDownMenu: Spinner
    private lateinit var questionBox: LinearLayout
    private lateinit var questionText: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var radioButtonFirstAnswer: AppCompatRadioButton
    private lateinit var radioButtonSecondAnswer: AppCompatRadioButton
    private lateinit var radioButtonThirdAnswer: AppCompatRadioButton
    private lateinit var radioButtonFourthAnswer: AppCompatRadioButton
    private lateinit var btnSubmitAnswer: Button
    private lateinit var btnNextQuestion: MaterialButton
    private lateinit var correctAnswer: String
    private lateinit var serialCorrectAnswersTextView: TextView
    private var chosenCategoryOption: String = "No Category"
    private var questionPosition = 0
    private var isYourAnswerCorrect: Boolean = false
    private var isButtonSelected: Boolean = false
    private var isButtonNextQuestionClicked = true
    var serialCorrectAnswers: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        initViews()
        //initSpinner()
        apiQuizViewModel.refreshQuestion()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNewQuestion()
        onRadioButtonsClicked()
        onSubmitButtonClicked()
    }

    private fun initViews() {
        dropDownMenu = binding.dropDownMenu
        questionBox = binding.questionBox
        questionText = binding.exampleQuestion
        answersRadioGroup = binding.radioGroup
        radioButtonFirstAnswer = binding.firstAnswerButton
        radioButtonSecondAnswer = binding.secondAnswerButton
        radioButtonThirdAnswer = binding.thirdAnswerButton
        radioButtonFourthAnswer = binding.fourthAnswerButton
        btnSubmitAnswer = binding.btnSubmitAnswer
        btnNextQuestion = binding.btnNextQuestion


//        serialCorrectAnswersTextView = binding.questionCounter

    }

    private fun initSpinner() {

        val spinnerColor = ContextCompat.getColor(requireContext(), R.color.color_wrong_answer)
        val options = listOf(
            "Books",
            "Films",
            "Music",
            "Theatres",
            "Video Games",
            "Science & Nature",
            "Computers",
            "Mathematics",
            "Mythology",
            "Sports",
            "Geography",
            "History",
            "Politics",
            "Art",
            "Celebrities",
            "Animals",
            "Vehicles",
            "Comics",
            "Anime & Manga",
            "Cartoon & Animations"
        )

        dropDownMenu.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)
        dropDownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenCategoryOption = options[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Please select option!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // TODO: 3.2.2022 г. Тази функция трябва да бъде поставена под getNewQuestion()
    private fun onRadioButtonsClicked() {
//        val buttonCheckedTextColor =
//            ContextCompat.getColor(requireContext(), R.color.checked_buttons_textColor)
//        val buttonUnCheckedTextColor =
//            ContextCompat.getColor(requireContext(), R.color.unchecked_buttons_textColor)
        val btnSubmitActiveColor = ContextCompat.getColor(requireContext(), R.color.white)

        radioButtonFirstAnswer.setOnClickListener {
            if (radioButtonFirstAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonFirstAnswer.text.toString()
                isButtonSelected = true

                if (yourAnswer.contains(correctAnswer)) {
                    isYourAnswerCorrect = true
                    correctAnswer = "Неу"
                }
            }
        }

        radioButtonSecondAnswer.setOnClickListener {
            if (radioButtonSecondAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonSecondAnswer.text.toString()
                isButtonSelected = true

                if (yourAnswer.contains(correctAnswer)) {
                    isYourAnswerCorrect = true
                    correctAnswer = "Неу"
                }
            }
        }

        radioButtonThirdAnswer.setOnClickListener {
            if (radioButtonThirdAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonThirdAnswer.text.toString()
                isButtonSelected = true

                if (yourAnswer.contains(correctAnswer)) {
                    isYourAnswerCorrect = true
                    correctAnswer = "Неу"
                }
            }
        }

        radioButtonFourthAnswer.setOnClickListener {
            if (radioButtonFourthAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonFourthAnswer.text.toString()
                isButtonSelected = true

                if (yourAnswer.contains(correctAnswer)) {
                    isYourAnswerCorrect = true
                    correctAnswer = "Неу"
                }
            }
        }
    }

    private fun getNewQuestion() {

        apiQuizViewModel.questionLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(this.context, "Unsuccessful api call!", Toast.LENGTH_LONG).show()
                return@observe
            } else {

                if (questionPosition == 10) {
                    questionText.visibility = INVISIBLE
                    questionBox.visibility = INVISIBLE
                    answersRadioGroup.visibility = GONE
                    btnSubmitAnswer.visibility = GONE
                } else {
                    // Setting answers to random positions/buttons
                    when (val randomNumber = (0..3).random()) {
                        0 -> {
                            radioButtonFirstAnswer.text = response[questionPosition].correct_answer
                            radioButtonSecondAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber]
                            radioButtonThirdAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber + 1]
                            radioButtonFourthAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber + 2]
                        }
                        1 -> {
                            radioButtonFirstAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 1]
                            radioButtonSecondAnswer.text = response[questionPosition].correct_answer
                            radioButtonThirdAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber]
                            radioButtonFourthAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber + 1]
                        }
                        2 -> {
                            radioButtonFirstAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 2]
                            radioButtonSecondAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 1]
                            radioButtonThirdAnswer.text = response[questionPosition].correct_answer
                            radioButtonFourthAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber]
                        }
                        3 -> {
                            radioButtonFirstAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 3]
                            radioButtonSecondAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 2]
                            radioButtonThirdAnswer.text =
                                response[questionPosition].incorrect_answers[randomNumber - 1]
                            radioButtonFourthAnswer.text = response[questionPosition].correct_answer
                        }
                    } // The end of the condition
                    questionText.text = response[questionPosition].question
                    correctAnswer = response[questionPosition].correct_answer
                    questionPosition += 1
                }
            }
        }
    }

    private fun onSubmitButtonClicked() {
        btnSubmitAnswer.setOnClickListener {

            if (isButtonSelected) {
                setNextQuestion()
                if (isYourAnswerCorrect) {
                    Toast.makeText(this.context, "Correct answer!", Toast.LENGTH_LONG).show()
                    btnNextQuestion.strokeColor = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.button_checked
                    )
                    serialCorrectAnswers++
                } else {
                    Toast.makeText(
                        this.context,
                        "Wrong! The correct answer is ".plus(correctAnswer),
                        Toast.LENGTH_LONG
                    ).show()
                    btnNextQuestion.strokeColor = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.color_wrong_answer
                    )
                    serialCorrectAnswers = 0

                    val date = getCurrentDate()
                    val currentStat = QuizPoints(points = serialCorrectAnswers, dateTime = date)
                    roomQuizViewModel.insertStat(currentStat)
                }

                btnNextQuestion.visibility = VISIBLE
                btnSubmitAnswer.visibility = GONE
//
                radioButtonFirstAnswer.isEnabled = false
                radioButtonSecondAnswer.isEnabled = false
                radioButtonThirdAnswer.isEnabled = false
                radioButtonFourthAnswer.isEnabled = false
            } else {
                Toast.makeText(this.context, "Please select answer!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setNextQuestion() {
        btnNextQuestion.setOnClickListener {
            isButtonNextQuestionClicked = false
            onGetNewQuestion()
        }

        if (isButtonNextQuestionClicked) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                onGetNewQuestion()
            }, 2500)
            isButtonNextQuestionClicked = true
        }
    }

    private fun onGetNewQuestion() {
        val colorUnselected = ContextCompat.getColor(requireContext(), R.color.unselected_color)

        getNewQuestion()
        isButtonSelected = false
        isYourAnswerCorrect = false

        btnNextQuestion.visibility = GONE

        btnSubmitAnswer.visibility = VISIBLE
        btnSubmitAnswer.setHintTextColor(colorUnselected)

        radioButtonFirstAnswer.isEnabled = true
        radioButtonSecondAnswer.isEnabled = true
        radioButtonThirdAnswer.isEnabled = true
        radioButtonFourthAnswer.isEnabled = true

        radioButtonFirstAnswer.isChecked = false
        radioButtonSecondAnswer.isChecked = false
        radioButtonThirdAnswer.isChecked = false
        radioButtonFourthAnswer.isChecked = false

        radioButtonFirstAnswer.isSelected = false
        radioButtonSecondAnswer.isSelected = false
        radioButtonThirdAnswer.isSelected = false
        radioButtonFourthAnswer.isSelected = false
    }

    private fun getCurrentDate(): String? {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+2")
        return dateFormat.format(currentTime)
    }
}
//}
