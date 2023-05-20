package ViewModel

import DAO.AnswerDao
import DAO.QuestionDao
import DAO.QuizHistoryDao
import DAO.ResultDao
import Model.Question
import Model.User_answer
import Model.user
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.duan.R
import com.google.gson.Gson
import kotlin.properties.Delegates


class QuestionActivity : AppCompatActivity() {
    private lateinit var questionImageView: ImageView
    private lateinit var questionTextView: TextView
    private lateinit var option1RadioButton: RadioButton
    private lateinit var option2RadioButton: RadioButton
    private lateinit var option3RadioButton: RadioButton
    private lateinit var option4RadioButton: RadioButton
    private lateinit var rbgroup: RadioGroup
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    var questioncount by Delegates.notNull<Int>()

    private lateinit var question_list: List<Question>
    private lateinit var traloi_list: ArrayList<User_answer>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        khoitao()
        showQuestion()
        btn2.setOnClickListener {
            traloi()
            if (questioncount == 19) {
                ketqua()
            } else {
                questioncount++
                showQuestion()
            }

        }
        btn1.setOnClickListener {
            traloi()
            questioncount--
            showQuestion()
        }
    }

    fun showQuestion() {
        showbutton()
        if (questioncount < 20) {
            val question = question_list.get(questioncount)
            val anwer = question.list_answer
            if (anwer.size > 0) {
                if (question.imageLink == null || question.imageLink.length == 0 || question.imageLink.isNullOrEmpty()) {
                    questionImageView.visibility = View.GONE
                } else {
                    questionImageView.visibility = View.VISIBLE
                }
                option1RadioButton.text = anwer.get(0).content
                option2RadioButton.text = anwer.get(1).content
                option3RadioButton.text = anwer.get(2).content
                option4RadioButton.text = anwer.get(3).content
            }

            questionTextView.text =
                "Câu hỏi " + (questioncount + 1).toString() + ": " + question.content
            var check =true
            for (i in traloi_list) {
                if (i.questionId == questioncount) {
                    check=false
                    if (i.idradio == -1)
                        rbgroup.clearCheck()
                    else {

                        rbgroup.check(i.idradio)
                    }
                    break
                }
            }
            if(check){
                rbgroup.clearCheck()
            }
        }
    }

    fun showbutton(){
        if (questioncount == 0) {
            btn1.visibility = View.GONE
            btn2.text = "Tiếp theo"
        } else if (questioncount == 19) {

            btn1.text = "Quay lại"
            btn2.text = "Nộp bài"
        } else {
            btn1.visibility = View.VISIBLE
            btn1.text = "Quay lại"
            btn2.text = "Tiếp theo"
        }

    }

    fun ketqua() {
        var diem = 0
        for (i in traloi_list) {
            if (AnswerDao(this).checkkq(i.idAnswer)) {
                diem += 5
            }
        }
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val gson = Gson()

        val userJsonString = sharedPreferences.getString("user", null)
        if (userJsonString != null) {
            val u = gson.fromJson(userJsonString, user::class.java)
            ResultDao(this).addResult(diem, u.id)
        }


        for (i in traloi_list) {
            QuizHistoryDao(this).addQuizHistory(i.questionId, i.idAnswer, ResultDao(this).getlastid())
        }
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("Diem", diem)
        startActivity(intent)
        finish()
    }





    fun traloi() {
        val optionsRadioGroup = findViewById<RadioGroup>(R.id.options_radio_group)
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId

        if (selectedOptionId != -1) {
            val selectedOption = findViewById<RadioButton>(selectedOptionId)
            val selectedOptionText = selectedOption.text.toString()
            var j = 0
            var check = true
            for (traloi in traloi_list) {
                if (traloi.questionId == questioncount) {
                    for (i in question_list.get(questioncount).list_answer) {
                        if (i.content == selectedOptionText) {
                            traloi_list.set(j, User_answer(questioncount, selectedOptionId, i.id))
                            check = false
                            break
                        }
                    }
                    break
                }
                j++
            }
            if (check) {
                var check2 =true
                for (i in question_list.get(questioncount).list_answer) {
                    if (i.content == selectedOptionText) {
                        traloi_list.add(User_answer(questioncount, selectedOptionId, i.id))
                        check2= false
                        break
                    }
                }
                if(check2){
                    traloi_list.add(User_answer(questioncount, -1, -1))
                }
            }
        }
    }

    fun khoitao() {
        // Ánh xạ các thành phần giao diện
        questionImageView = findViewById(R.id.question_image)
        questionTextView = findViewById(R.id.question_text)
        option1RadioButton = findViewById(R.id.option_1)
        option2RadioButton = findViewById(R.id.option_2)
        option3RadioButton = findViewById(R.id.option_3)
        option4RadioButton = findViewById(R.id.option_4)
        rbgroup = findViewById(R.id.options_radio_group)
        question_list = QuestionDao(this).get20Questions()
        questioncount = 0
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
//        Collections.shuffle(question_list)
        traloi_list = ArrayList<User_answer>()
    }
}
