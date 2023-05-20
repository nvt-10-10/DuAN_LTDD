package connect



import Adapter.AnswersAdapter

import android.database.sqlite.SQLiteDatabase
import android.content.Context

import android.database.sqlite.SQLiteOpenHelper

class  DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "duan1"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        // Tạo bảng Topic Category
        val createTableTopicCategory = "CREATE TABLE topic_category (" +
                "id_topic INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL" +
                ");"
        db?.execSQL(createTableTopicCategory)

// Tạo bảng Question
        val createTableQuestion = "CREATE TABLE question (" +
                "id_question INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT NOT NULL, " +
                "id_topic INTEGER NOT NULL, " +
                "image_link TEXT, " +
                "FOREIGN KEY(id_topic) REFERENCES topic_category(id_topic)" +
                ");"
        db?.execSQL(createTableQuestion)


// Tạo bảng Answer
        val createTableAnswer = "CREATE TABLE answer (" +
                "id_answer INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT NOT NULL, " +
                "is_correct INTEGER NOT NULL, " +
                "id_question INTEGER NOT NULL, " +
                "Explain TEXT NULL, " + // Added missing comma here
                "FOREIGN KEY(id_question) REFERENCES question(id_question)" +
                ");"

        db?.execSQL(createTableAnswer)

// Tạo bảng User
        val createTableUser = "CREATE TABLE user (" +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "name TEXT NOT NULL" +
                ");"
        db?.execSQL(createTableUser)

// Tạo bảng Result
        val createTableResult = "CREATE TABLE result (" +
                "id_result INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER NOT NULL, " +
                "id_user INTEGER NOT NULL, " +
                "quiz_date TEXT NOT NULL, " +
                "FOREIGN KEY(id_user) REFERENCES user(id_user)" +
                ");"
        db?.execSQL(createTableResult)

// Tạo bảng Quiz History
        val createTableQuizHistory = "CREATE TABLE quiz_history (" +
                "id_quiz_history INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_question INTEGER NOT NULL, " +
                "id_answer INTEGER NOT NULL, " +
                "id_result INTEGER NOT NULL, " +
                "FOREIGN KEY(id_question) REFERENCES question(id_question), " +
                "FOREIGN KEY(id_answer) REFERENCES answer(id_answer), " +
                "FOREIGN KEY(id_result) REFERENCES result(id_result)" +
                ");"
        db?.execSQL(createTableQuizHistory)




    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS quiz_history")
        db?.execSQL("DROP TABLE IF EXISTS result")
        db?.execSQL("DROP TABLE IF EXISTS answer")
        db?.execSQL("DROP TABLE IF EXISTS question")
        db?.execSQL("DROP TABLE IF EXISTS topic_category")
        onCreate(db)
    }


}

