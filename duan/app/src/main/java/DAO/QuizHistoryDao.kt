package DAO

import Model.QuizHistory
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler

class QuizHistoryDao(context: Context) {
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase

    fun addQuizHistory(
        id_question: Int,
        id_answer: Int,
        id_result: Int
    ): Boolean {
        val values = ContentValues().apply {
            put("id_question", id_question)
            put("id_answer", id_answer)
            put("id_result", id_result)
        }

        val rowId = db.insert("quiz_history", null, values)
        return rowId != -1L
    }

    fun updateQuizHistory(
        id_quiz_history: Int,
        id_question: Int,
        id_answer: Int,
        id_result: Int
    ): Boolean {
        val values = ContentValues().apply {
            put("id_question", id_question)
            put("id_answer", id_answer)
            put("id_result", id_result)
        }

        val rowsAffected = db.update(
            "quiz_history", values, "id_quiz_history=?",
            arrayOf(id_quiz_history.toString())
        )
        return rowsAffected > 0
    }

    fun deleteQuizHistory(id_quiz_history: Int): Boolean {
        val rowsAffected = db.delete(
            "quiz_history", "id_quiz_history=?",
            arrayOf(id_quiz_history.toString())
        )
        return rowsAffected > 0
    }

    @SuppressLint("Range")
    private fun cursorToQuizHistory(cursor: Cursor): QuizHistory {
        return QuizHistory(
            cursor.getInt(cursor.getColumnIndex("id_quiz_history")),
            cursor.getInt(cursor.getColumnIndex("id_question")),
            cursor.getInt(cursor.getColumnIndex("id_answer")),
            cursor.getInt(cursor.getColumnIndex("id_result"))
        )
    }

    fun getAllQuizHistories(): List<QuizHistory> {
        val quizHistories = mutableListOf<QuizHistory>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM quiz_history", null)

        if (cursor.moveToFirst()) {
            do {
                quizHistories.add(cursorToQuizHistory(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return quizHistories
    }

    fun getQuizHistoryById(id_quiz_history: Int): QuizHistory? {
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM quiz_history WHERE id_quiz_history=?",
            arrayOf(id_quiz_history.toString())
        )

        val quizHistory: QuizHistory? = if (cursor.moveToFirst()) {
            cursorToQuizHistory(cursor)
        } else {
            null
        }

        cursor.close()
        return quizHistory
    }

    fun getQuizHistoriesByQuestionId(id_question: Int): List<QuizHistory> {
        val quizHistories = mutableListOf<QuizHistory>()
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM quiz_history WHERE id_question=?",
            arrayOf(id_question.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                quizHistories.add(cursorToQuizHistory(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return quizHistories
    }
}
