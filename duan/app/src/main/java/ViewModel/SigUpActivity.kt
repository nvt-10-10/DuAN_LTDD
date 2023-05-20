package ViewModel

import DAO.UserDao
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.duan.R
import com.google.gson.Gson

class SigUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sig_up)
        findViewById<Button>(R.id.btn_register).setOnClickListener {
            val username = findViewById<TextView>(R.id.et_email).text.toString()
            val password = findViewById<TextView>(R.id.et_password).text.toString()
            val name = findViewById<TextView>(R.id.et_name).text.toString()

            val udao = UserDao(this)
            if(udao.addUser(username,password, name )){
                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val u=udao.checkLogin(username,password)
               if(u!=null){
                   val gson = Gson()
                   val userString=gson.toJson(u)
                   editor.putString("user",userString)
                   editor.apply()

                   // In thông tin người dùng ra Logcat
                   Log.d("SignUpActivity", "User ID: ${u.id}, Username: ${u.username}, Name: ${u.name}")
               }
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()

                Toast.makeText(this, "Đăng kí  thành công", Toast.LENGTH_SHORT).show()
            } else{
                // In thông tin người dùng ra Logcat
                Log.d("SignUpActivity", "User ID:")
                Toast.makeText(this, "User đã tồn tại", Toast.LENGTH_SHORT).show()
            }

        }
    }
}