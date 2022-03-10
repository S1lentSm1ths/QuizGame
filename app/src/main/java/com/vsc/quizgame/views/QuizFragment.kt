package com.vsc.quizgame.views

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.vsc.quizgame.R
import com.vsc.quizgame.data.QuizPoints
import com.vsc.quizgame.databinding.FragmentQuizBinding
import com.vsc.quizgame.view.FragmentScoreboard
import com.vsc.quizgame.view.viewmodels.ApiQuizViewModel
import com.vsc.quizgame.viewmodels.RoomQuizViewModel
import kotlinx.coroutines.*
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import com.vsc.quizgame.api.NetworkConnection
import com.vsc.quizgame.view.api.Question


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
    private lateinit var btnSubmitAnswer: MaterialButton
    private lateinit var btnNextQuestion: MaterialButton
    private lateinit var btnContinue: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var networkInfoBox: ConstraintLayout
    private lateinit var internetConnectedImage: ImageView
    private lateinit var internetConnectedText: TextView
    private lateinit var internetDisconnectedImage: ImageView
    private lateinit var internetDisconnectedText: TextView
    private lateinit var correctAnswer: String
    private var chosenCategoryOption: String = "No Category"
    private var chosenCategoryNumber: Int = 22
    private var questionPosition: Int = 0
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
        apiQuizViewModel.refreshQuestion(chosenCategoryNumber)
        initSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataToViews()
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
        btnContinue = binding.btnContinueCurrentCategory
        btnCancel = binding.btnCancelCurrentCategory
        networkInfoBox = binding.networkInfoBox
        internetConnectedImage = binding.imageConnectedImage
        internetConnectedText = binding.internetConnectedText
        internetDisconnectedImage = binding.internetDisconnectedImage
        internetDisconnectedText = binding.internetDisconnectedText
    }

    private fun initSpinner() {

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
                getNumberOfCategory()
                questionPosition = 0
                apiQuizViewModel.refreshQuestion(chosenCategoryNumber)
                setDataToViews()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Please select option!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getNumberOfCategory() {
        chosenCategoryNumber = when (chosenCategoryOption) {
            "Books" -> {
                10
            }
            "Films" -> {
                11
            }
            "Music" -> {
                12
            }
            "Theatres" -> {
                13
            }
            "Video Games" -> {
                15
            }
            "Science & Nature" -> {
                17
            }
            "Computers" -> {
                18
            }
            "Mathematics" -> {
                19
            }
            "Mythology" -> {
                20
            }
            "Sports" -> {
                21
            }
            "Geography" -> {
                22
            }
            "History" -> {
                23
            }
            "Politics" -> {
                24
            }
            "Art" -> {
                25
            }
            "Celebrities" -> {
                26
            }
            "Animals" -> {
                27
            }
            "Vehicles" -> {
                28
            }
            "Comics" -> {
                29
            }
            "Anime & Manga" -> {
                31
            }
            "Cartoon & Animations" -> {
                32
            }
            else -> 22
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
                checkSelectedAnswer(yourAnswer)
            }
        }

        radioButtonSecondAnswer.setOnClickListener {
            if (radioButtonSecondAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonSecondAnswer.text.toString()
                isButtonSelected = true
                checkSelectedAnswer(yourAnswer)
            }
        }

        radioButtonThirdAnswer.setOnClickListener {
            if (radioButtonThirdAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonThirdAnswer.text.toString()
                isButtonSelected = true
                checkSelectedAnswer(yourAnswer)
            }
        }

        radioButtonFourthAnswer.setOnClickListener {
            if (radioButtonFourthAnswer.isChecked) {
                btnSubmitAnswer.setHintTextColor(btnSubmitActiveColor)
                val yourAnswer = radioButtonFourthAnswer.text.toString()
                isButtonSelected = true
                checkSelectedAnswer(yourAnswer)
            }
        }
    }

    private fun checkSelectedAnswer(yourAnswer: String) {
        if (yourAnswer.contains(correctAnswer)) {
            isYourAnswerCorrect = true
            correctAnswer = "Неу"
        }
    }

    private fun onEndOfQuestions() {
        insertStat()
        hideAndDisplayLastQuestionViews()

        btnContinue.setOnClickListener {
            questionPosition = 0
            apiQuizViewModel.refreshQuestion(chosenCategoryNumber)
            setDataToViews()
        }
        btnCancel.setOnClickListener {
            Toast.makeText(this.context, "Please select category!", Toast.LENGTH_LONG).show()
        }
    }

    private fun hideAndDisplayLastQuestionViews() {
        answersRadioGroup.visibility = GONE
        btnSubmitAnswer.visibility = GONE

        questionText.text = getString(R.string.do_you_want_to_continue)
        btnContinue.visibility = VISIBLE
        btnCancel.visibility = VISIBLE
    }

    private fun setDataToViews() {
        apiQuizViewModel.questionLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(this.context, "Unsuccessful api call!", Toast.LENGTH_LONG).show()
                return@observe
            } else {

                if (questionPosition == 10) {
                    onEndOfQuestions()
                } else {
                    hideAndDisplayNewQuestionViews()
                    setAnswersRandomPositions(response)
                    questionText.text = response[questionPosition].question
                    correctAnswer = response[questionPosition].correct_answer
                }
            }
        }
    }

    private fun hideAndDisplayNewQuestionViews() {
        answersRadioGroup.visibility = VISIBLE
        btnContinue.visibility = GONE
        btnCancel.visibility = GONE
        btnSubmitAnswer.visibility = VISIBLE
    }

    private fun setAnswersRandomPositions(response: List<Question>) {
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
        }
    }

    private fun onSubmitButtonClicked() {
        btnSubmitAnswer.setOnClickListener {
            questionPosition++
            if (isButtonSelected) {
                setNextQuestion()
                if (isYourAnswerCorrect) {
                    onCorrectAnswer()
                } else {
                    onWrongAnswer()
                }

                btnNextQuestion.visibility = VISIBLE
                btnSubmitAnswer.visibility = GONE
//
                disableRadioButtons()
            } else {
                Toast.makeText(this.context, "Please select answer!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onCorrectAnswer() {
        Toast.makeText(this.context, "Correct answer!", Toast.LENGTH_LONG).show()
        btnNextQuestion.strokeColor = ContextCompat.getColorStateList(
            requireContext(),
            R.color.button_checked
        )
        serialCorrectAnswers++
    }

    private fun onWrongAnswer() {
        Toast.makeText(
            this.context,
            "Wrong! The correct answer is ".plus(correctAnswer),
            Toast.LENGTH_LONG
        ).show()
        btnNextQuestion.strokeColor = ContextCompat.getColorStateList(
            requireContext(),
            R.color.color_wrong_answer
        )

        insertStat()

        serialCorrectAnswers = 0
    }

    private fun disableRadioButtons() {
        radioButtonFirstAnswer.isEnabled = false
        radioButtonSecondAnswer.isEnabled = false
        radioButtonThirdAnswer.isEnabled = false
        radioButtonFourthAnswer.isEnabled = false
    }

    private fun insertStat() {
        val date = getCurrentDate()
        val currentStat = QuizPoints(points = serialCorrectAnswers, dateTime = date)
        roomQuizViewModel.insertStat(currentStat)
    }

    private fun setNextQuestion() {
        btnNextQuestion.setOnClickListener {
            isButtonNextQuestionClicked = false
            onGotNewQuestion()
        }
    }
//        if (isButtonNextQuestionClicked) {
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                onGotNewQuestion()
//            }, 2500)
//            isButtonNextQuestionClicked = true
//        }


    private fun onGotNewQuestion() {
        val colorUnselected = ContextCompat.getColor(requireContext(), R.color.unselected_color)

        setDataToViews()
        isButtonSelected = false
        isYourAnswerCorrect = false

        btnNextQuestion.visibility = GONE

        prepareButtonsForNextQuestion(colorUnselected)
    }

    private fun prepareButtonsForNextQuestion(colorUnselected: Int) {
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

//    private fun swipeToRefresh() {
//        swipeToRefresh.setOnRefreshListener {
//            Toast.makeText(this.context, "Refreshed", Toast.LENGTH_LONG).show()
//
//            swipeToRefresh.isRefreshing = false
//        }
//    }

//    private fun ifNetworkConnected(): Boolean {
//        var connected = false
//        val networkConnection = NetworkConnection(requireContext())
//        networkConnection.observe(this, androidx.lifecycle.Observer { isConnected ->
//            connected = isConnected
//        })
//        return connected
//    }
//
//    private fun displayNetworkInformation(isConnected: Boolean) {
//        if (isConnected) {
//            networkInfoBox.visibility = GONE
//            internetConnectedImage.visibility = GONE
//            internetConnectedText.visibility = GONE
//            internetDisconnectedImage.visibility = GONE
//            internetDisconnectedText.visibility = GONE
//            val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                networkInfoBox.visibility = GONE
//            }, 2500)
//        } else {
//            networkInfoBox.visibility = VISIBLE
//            internetConnectedImage.visibility = GONE
//            internetConnectedText.visibility = GONE
//            internetDisconnectedImage.visibility = VISIBLE
//            internetDisconnectedText.visibility = VISIBLE
//        }
//    }
//
//    private fun getApiRequest(){
//        if (ifNetworkConnected()) {
//            apiQuizViewModel.refreshQuestion(chosenCategoryNumber)
//        }
//        else {
//            displayNetworkInformation(ifNetworkConnected())
//        }
//    }
}
//}
