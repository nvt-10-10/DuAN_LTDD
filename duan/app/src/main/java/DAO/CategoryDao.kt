package DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler
import Model.Category
import android.annotation.SuppressLint

class CategoryDao(context: Context) {
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase

    fun addCategory(name: String): Boolean {
        val cursor = db.rawQuery("SELECT * FROM topic_category WHERE name=?", arrayOf(name))
        return if (cursor.count > 0) {
            cursor.close()
            false
        } else {
            val values = ContentValues().apply {
                put("name", name)
            }
            db.insert("topic_category", null, values)
            cursor.close()
            true
        }
    }

    fun updateCategory(id: Int, name: String) {
        val values = ContentValues().apply {
            put("name", name)
        }

        db.update("topic_category", values, "id_topic=?", arrayOf(id.toString()))
    }

    fun deleteCategory(id: Int) {
        db.delete("topic_category", "id_topic=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM topic_category", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id_topic"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                categories.add(Category(id, name))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return categories
    }
}
