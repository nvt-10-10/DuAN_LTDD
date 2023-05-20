package Adapter

import DAO.AnswerDao
import Model.Answer
import Model.User_answer
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.duan.R

class AnswersAdapter(
    private val answers: List<Answer>,
    private val recyclerView: RecyclerView,
    var list_User_answer: ArrayList<User_answer>
) :
    RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>() {

    private var selectedAnswerPosition = RecyclerView.NO_POSITION


    private var checkedAnswerPosition = RecyclerView.NO_POSITION
    private var checkedAnswerId: Int = -1
    private var selectedRadioButton: RadioButton? = null
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val viewItem = inflater.inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(viewItem, list_User_answer, context)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers[position]
        holder.bind(answer)
        Log.d("onBindViewHolder: ", itemCount.toString())
        holder.answerRadioButton.isChecked = selectedAnswerPosition == position
        holder.answerRadioButton.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                clearSelectedAnswer(holder)
                setSelectedAnswer(clickedPosition,holder)
                holder.setColorSelectAnwer(answer)
            }
        }
    }



    override fun getItemCount(): Int {
        return answers.size
    }

    fun getSelectedAnswerContent(): String? {
        return if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
            answers[selectedAnswerPosition].content
        } else {
            null
        }
    }

    fun getSelectedRadioButtonId(): Int {
        return selectedAnswerPosition
    }

    private fun setSelectedAnswer(position: Int,holder: AnswerViewHolder) {
        val answer = answers[position]
        if (selectedAnswerPosition != RecyclerView.NO_POSITION) {
            selectedRadioButton?.isChecked = false
        }
        for (i in list_User_answer){
            if (i.questionId==answer.questionId){
                holder.setColorSelectAnwer(answer)
            }
        }
        selectedAnswerPosition = position
        selectedRadioButton = null
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceAsColor")
    private fun clearSelectedAnswer(holder: AnswerViewHolder) {
        selectedAnswerPosition = RecyclerView.NO_POSITION
        resetcolor()
        notifyDataSetChanged()

    }


    @SuppressLint("NotifyDataSetChanged")
    fun setCheckedAnswer(answerId: Int) {
        checkedAnswerId = answerId
        checkedAnswerPosition = answers.indexOfFirst { it.id == answerId }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    fun clearCheckedAnswer() {
        checkedAnswerId = -1
        checkedAnswerPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()

        val list_radio = getRadioButtons()
        Log.d("clearCheckedAnswer", list_radio.size.toString())
        Log.d("clearCheckedAnswer", itemCount.toString())
        for (i in list_radio) {
            i.setTextColor(R.color.black)
        }
    }

    fun getRadioButtons(): List<RadioButton> {
        val radioButtons = mutableListOf<RadioButton>()
        for (i in 0 until itemCount) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? AnswerViewHolder
            viewHolder?.let {
                radioButtons.add(it.answerRadioButton)
            }
        }
        return radioButtons
    }

    @SuppressLint("ResourceAsColor")
    fun resetcolor() {
        val RadioButtons = getRadioButtons()
        for (i in RadioButtons) {
            i.setTextColor(Color.parseColor("#050505"))

        }
    }


    class AnswerViewHolder(
        itemView: View,
        var list_User_answer: ArrayList<User_answer>,
        var context: Context
    ) : RecyclerView.ViewHolder(itemView) {
        val answerRadioButton: RadioButton = itemView.findViewById(R.id.answer_radio_button)

        fun bind(answer: Answer) {
            Log.d("soze", list_User_answer.size.toString())
            var ischeck = true
            for (i in list_User_answer) {
                if (i.questionId == answer.questionId) {
                    answerRadioButton.setChecked(true)
                    setColorSelectAnwer(answer)
                    ischeck = false
                    break
                }
            }
            answerRadioButton.text = answer.content
            if (ischeck) {
                answerRadioButton.setTextColor(Color.parseColor("#050505"))
            }
        }
        fun setColorSelectAnwer(answer :Answer){
            val answerDao = AnswerDao(context) // Pass the context
            if (answerDao.checkAnswer(answer.id)) {
                answerRadioButton.setTextColor(Color.parseColor("#03DAC5"))
            } else {
                answerRadioButton.setTextColor(Color.parseColor("#FA0606"))
            }
        }
    }
}
