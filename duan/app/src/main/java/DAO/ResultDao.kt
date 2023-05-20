package DAO

import Model.Result
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler
import java.util.Calendar

class ResultDao(context: Context) {
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase

    fun addResult(score: Int, id_user: Int) {
        val values = ContentValues().apply {
            put("score", score)
            put("id_user", id_user)
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0 nên cần cộng thêm 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val currentDate = "$year-$month-$day"
            put("quiz_date", currentDate)
        }

        db.insert("result", null, values)
    }

    fun updateResult(id_result: Int, score: Int, id_user: Int, quiz_date: String) {
        val values = ContentValues().apply {
            put("score", score)
            put("id_user", id_user)
            put("quiz_date", quiz_date)
        }

        db.update("result", values, "id_result=?", arrayOf(id_result.toString()))
    }

    fun deleteResult(id_result: Int) {
        db.delete("result", "id_result=?", arrayOf(id_result.toString()))
    }

    @SuppressLint("Range")
    fun getAllResults(): List<Result> {
        val results = mutableListOf<Result>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM result", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id_result"))
                val score = cursor.getInt(cursor.getColumnIndex("score"))
                val id_user = cursor.getInt(cursor.getColumnIndex("id_user"))
                val quiz_date = cursor.getString(cursor.getColumnIndex("quiz_date"))

                results.add(Result(id, score, id_user, quiz_date))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return results
    }

    fun getlastid(): Int {
        val cursor = db.rawQuery("SELECT id_result FROM result ORDER BY id_result DESC LIMIT 1",null)
        cursor.moveToFirst()
        val lastid = cursor.getInt(0)
        cursor.close()
        return lastid
    }
}
