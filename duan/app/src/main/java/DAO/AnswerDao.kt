package DAO

import Model.Answer
import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler
import android.content.Context


class AnswerDao(context: Context) {
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase


    fun addAnswer(content: String, isCorrect: Int, questionId: Int, explain: String?) {
        val values = ContentValues().apply {
            put("content", content)
            put("is_correct", isCorrect)
            put("id_question", questionId)
            put("explain", explain)
        }

        db.insert("answer", null, values)
    }

    fun updateAnswer(answer: Answer) {
        val values = ContentValues().apply {
            put("content", answer.content)
            put("is_correct", answer.isCorrect)
            put("id_question", answer.questionId)
            put("explain", answer.explain)
        }

        db.update("answer", values, "id_answer=?", arrayOf(answer.id.toString()))
    }

    fun deleteAnswer(id: Int) {
        db.delete("answer", "id_answer=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getAnswersByQuestionId(questionId: Int): List<Answer> {
        val answers = mutableListOf<Answer>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM answer WHERE id_question = ?", arrayOf(questionId.toString()))

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id_answer")
            val contentIndex = cursor.getColumnIndex("content")
            val isCorrectIndex = cursor.getColumnIndex("is_correct")
            val explainIndex = cursor.getColumnIndex("explain")

            do {
                val id = cursor.getInt(idIndex)
                val content = cursor.getString(contentIndex)
                val isCorrect = cursor.getInt(isCorrectIndex)
                val explain = if (explainIndex != -1) cursor.getString(explainIndex) else ""

                answers.add(Answer(id, content, isCorrect, questionId, explain))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return answers
    }


    fun deleteAll() {
        db.execSQL("DELETE FROM answer")
    }

    @SuppressLint("Range")
    fun checkAnswer(id_answer: Int): Boolean {
        var answer: Answer? = null
        val cursor = db.rawQuery("SELECT * FROM answer WHERE id_answer = ?", arrayOf(id_answer.toString()))
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("id_answer"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val isCorrect = cursor.getInt(cursor.getColumnIndex("is_correct"))
            val questionId = cursor.getInt(cursor.getColumnIndex("id_question"))
            val explainIndex = cursor.getColumnIndex("explain")
            val explain = if (explainIndex != -1) cursor.getString(explainIndex) else ""

            answer = Answer(id, content, isCorrect, questionId, explain)
        }
        cursor.close()

        return answer?.isCorrect == 1
    }
    fun deleteall(){
        db.execSQL("DELETE FROM answer")
    }
    @SuppressLint("Range")
    fun checkkq( id_answer :Int ): Boolean
    {
        var answer: Answer? = null
        val cursor = db.rawQuery("SELECT * FROM answer WHERE id_answer = ?", arrayOf(id_answer.toString()))
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("id_answer"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val isCorrect = cursor.getInt(cursor.getColumnIndex("is_correct"))
            val questionId = cursor.getInt(cursor.getColumnIndex("id_question"))
            val explain = cursor.getString(cursor.getColumnIndex("explain"))
            answer = Answer(id, content, isCorrect, questionId,explain)
        }
        cursor.close()
        if (answer != null) {
            if(answer.isCorrect==1)
                return true

        }
        return false
    }
}
