package ViewModel

import Adapter.AnswersAdapter

import DAO.QuestionDao

import Model.Answer
import Model.Question
import Model.User_answer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duan.R
import kotlin.properties.Delegates


class LearningActivity : AppCompatActivity() {
    private lateinit var questionImageView: ImageView
    private lateinit var questionTextView: TextView
    private lateinit var answersRecyclerView: RecyclerView
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private var questionCount by Delegates.notNull<Int>()

    private lateinit var questionList: List<Question>
    private lateinit var userAnswerList: ArrayList<User_answer>
    private lateinit var answersAdapter: AnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)
        initializeViews()
        showQuestion()

        btn2.setOnClickListener {
            handleNextButtonClick()
        }

        btn1.setOnClickListener {
            handlePreviousButtonClick()
        }
    }

    private fun initializeViews() {
        questionImageView = findViewById(R.id.question_image)
        questionTextView = findViewById(R.id.question_text)
        answersRecyclerView = findViewById(R.id.answers_recycler_view)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)

        questionList = QuestionDao(this).getAllQuestions()
        questionCount = 0
        userAnswerList = ArrayList()
        answersAdapter = AnswersAdapter( ArrayList<Answer>(),answersRecyclerView,userAnswerList)

        val layoutManager = LinearLayoutManager(this)
        answersRecyclerView.layoutManager = layoutManager
        answersRecyclerView.adapter = answersAdapter
    }

    private fun showQuestion() {
        showButton()

        if (questionCount < questionList.size) {
            val question = questionList[questionCount]
            val answers = question.list_answer

            questionTextView.text = "Câu hỏi ${questionCount + 1}: ${question.content}"

            if (answers.isNotEmpty()) {

                answersAdapter= AnswersAdapter( answers,answersRecyclerView, userAnswerList)
                answersRecyclerView.adapter = answersAdapter
                answersRecyclerView.layoutManager= GridLayoutManager(this,1)
                answersRecyclerView.visibility = View.VISIBLE
            } else {
                answersRecyclerView.visibility = View.GONE
            }

            var isCheckedAnswer = false
            var checkedAnswerId = -1
            for (userAnswer in userAnswerList) {
                if (userAnswer.questionId == questionCount) {
                    isCheckedAnswer = true
                    checkedAnswerId = userAnswer.idradio
                    break
                }
            }

            if (isCheckedAnswer) {
                answersAdapter.setCheckedAnswer(checkedAnswerId)
            } else {
                answersAdapter.clearCheckedAnswer()
            }
        }
    }

    private fun handleNextButtonClick() {
        saveUserAnswer()
        questionCount++
        showQuestion()
    }

    private fun handlePreviousButtonClick() {
        saveUserAnswer()
        questionCount--
        showQuestion()
    }

    private fun showButton() {
        when (questionCount) {
            0 -> {
                btn1.visibility = View.GONE
                btn2.text = "Tiếp theo"
            }
            questionList.size - 1 -> {
                btn1.text = "Quay lại"
                btn2.visibility = View.GONE
            }
            else -> {
                btn1.visibility = View.VISIBLE

                btn1.text = "Quay lại"
                btn2.text = "Tiếp theo"
            }
        }
    }

    private fun saveUserAnswer() {

        val selectedOptionId = answersAdapter.getSelectedRadioButtonId()
        val selectedOptionText = answersAdapter.getSelectedAnswerContent()

        if (selectedOptionId != -1 && selectedOptionText != null)
            if (selectedOptionId != -1 && selectedOptionText != null) {
                var isUserAnswerFound = false
                for (userAnswer in userAnswerList) {
                    if (userAnswer.questionId == questionCount) {
                        userAnswer.idradio = selectedOptionId
                        userAnswer.idAnswer = getAnswerIdByContent(questionList[questionCount].list_answer, selectedOptionText)
                        isUserAnswerFound = true
                        break
                    }
                }

                if (!isUserAnswerFound) {
                    userAnswerList.add(User_answer(questionCount, selectedOptionId, getAnswerIdByContent(questionList[questionCount].list_answer, selectedOptionText)))
                }
            }
    }

    private fun getAnswerIdByContent(answers: List<Answer>, content: String): Int {
        for (answer in answers) {
            if (answer.content == content) {
                return answer.id
            }
        }
        return -1
    }
}
