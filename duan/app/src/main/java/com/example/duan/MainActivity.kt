package com.example.duan

import DAO.AnswerDao
import DAO.CategoryDao
import DAO.QuestionDao
import ViewModel.LoginActivity
import DAO.UserDao
import Model.user
import ViewModel.HomeActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var u:user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val udao = UserDao(this)
        khoitao()
        if(QuestionDao(this).getAllQuestions().isEmpty()||QuestionDao(this).getAllQuestions().size==0){
            khoitaodl()
        }
        if (UserDao(this).checkLogin(u.username,u.password)!=null) {
            // Nếu người dùng đã đăng nhập trước đó, chuyển hướng sang màn hình chính
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Nếu người dùng chưa đăng nhập, chuyển hướng sang màn hình đăng nhập
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun khoitao(){
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val gson = Gson()

        val userJsonString = sharedPreferences.getString("user", null)
        if (userJsonString != null) {
            u = gson.fromJson(userJsonString, user::class.java)
        } else {
            u = user(1,"","","")
        }
    }

    fun khoitaodl(){
        CategoryDao(this).addCategory("De so 0")
        AnswerDao(this).deleteall()
        for (i in 0..20){
            QuestionDao(this).addQuestion("Day la cau hoi thu "+(i+1).toString(),0,"")
            for (j in 1..4){
                if (j<4)
                    AnswerDao(this).addAnswer("Dap an "+j.toString(),0,i,"")
                else
                    AnswerDao(this).addAnswer("Dap an "+j.toString(),1,i,"Day la dap an dung")
            }
        }
    }
}
