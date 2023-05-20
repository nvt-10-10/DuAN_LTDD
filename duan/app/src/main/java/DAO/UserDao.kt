package DAO

import Model.user
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import connect.DatabaseHandler

class UserDao(context: Context) {
    private val db: SQLiteDatabase = DatabaseHandler(context).writableDatabase

    fun addUser(username: String, password: String, name: String): Boolean {
        val cursor = db.rawQuery("SELECT * FROM user WHERE username=?", arrayOf(username))
        return if (cursor.count > 0) {
            cursor.close()
            false
        } else {
            val values = ContentValues().apply {
                put("username", username)
                put("password", password)
                put("name", name)
            }
            db.insert("user", null, values)
            cursor.close()
            true
        }
    }

    fun updateUser(id: Int, username: String, password: String, name: String) {
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
            put("name", name)
        }
        db.update("user", values, "id_user=?", arrayOf(id.toString()))
        print(db.toString())
    }

    fun deleteUser(id: Int) {
        db.delete("user", "id_user=?", arrayOf(id.toString()))
    }

    @SuppressLint("Range")
    fun getAllUsers(): List<user> {
        val users = mutableListOf<user>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM user", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id_user"))
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                println(id.toString()+"\t"+users+"\t"+password+"\t"+name)
                users.add(user(id, username, password, name))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return users
    }

    @SuppressLint("Range")
    fun checkLogin(username: String, password: String): user? {
        val selectQuery = "SELECT * FROM user WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(username, password))
        val count = cursor.count
        println(count)
        println(username+"\t"+password)
        val user: user? = if (cursor.moveToFirst()) {
            user(
                cursor.getInt(cursor.getColumnIndex("id_user")),
                cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("password")),
                cursor.getString(cursor.getColumnIndex("name"))
            )
        } else {
            null
        }
        cursor.close()
        return user
    }
}
