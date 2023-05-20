package DAO

import Model.Answer
import Model.Question
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler

class QuestionDao(context: Context) {
    private val context: Context = context.applicationContext
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase

    fun addQuestion(content: String, topicId: Int, imageLink: String?): Boolean {
        val values = ContentValues().apply {
            put("content", content)
            put("id_topic", topicId)
            put("image_link", imageLink)
        }

        db.insert("question", null, values)
        return true
    }

    fun updateQuestion(id: Int, content: String, topicId: Int, imageLink: String?) {
        val values = ContentValues().apply {
            put("content", content)
            put("id_topic", topicId)
            put("image_link", imageLink)
        }

        db.update("question", values, "id_question=?", arrayOf(id.toString()))
    }

    fun deleteQuestion(id: Int) {
        db.delete("question", "id_question=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun get20Questions(): List<Question> {
        val questions = mutableListOf<Question>()
        val selectQuery = "SELECT  * FROM question limit 20"

        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id_question"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val imageLink = cursor.getString(cursor.getColumnIndex("image_link"))
                val anwer_list = AnswerDao(context).getAnswersByQuestionId(id.toString().toInt())
                questions.add(Question(id, content, imageLink, anwer_list as ArrayList<Answer>))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    @SuppressLint("Range")
    fun getAllQuestions(): List<Question> {
        val questions = mutableListOf<Question>()
        val selectQuery = "SELECT  * FROM question "

        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id_question"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val imageLink = cursor.getString(cursor.getColumnIndex("image_link"))
                val anwer_list = AnswerDao(context).getAnswersByQuestionId(id.toString().toInt())
                questions.add(Question(id, content, imageLink, anwer_list as ArrayList<Answer>))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    fun count(): Int {
        val cursor = db.rawQuery("SELECT COUNT(*) FROM question", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }




//    @SuppressLint("Range")
//    fun getQuestionsByTopic(topicId: Int): List<Question> {
//        val questions = mutableListOf<Question>()
//        val selectQuery = "SELECT * FROM question WHERE id_topic = ?"
//
//        val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(topicId.toString()))
//        if (cursor.moveToFirst()) {
//            do {
//                val id = cursor.getInt(cursor.getColumnIndex("id_question"))
//                val content = cursor.getString(cursor.getColumnIndex("content"))
//                val topicId = cursor.getInt(cursor.getColumnIndex("id_topic"))
//                val imageLink = cursor.getString(cursor.getColumnIndex("image_link"))
//
//                questions.add(Question(id, content, topicId, imageLink))
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
//        return questions
//    }
}
