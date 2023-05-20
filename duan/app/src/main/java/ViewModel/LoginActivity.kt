package ViewModel

import DAO.UserDao
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.duan.R
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val username = findViewById<TextView>(R.id.et_email).text
            val password = findViewById<TextView>(R.id.et_password).text

            val u = UserDao(this).checkLogin(username.toString(),password.toString())
            if (u!=null){
                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val userString=gson.toJson(u)
                editor.putString("user",userString)
                editor.apply()
                editor.apply()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Đăng nhập  thành công", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }

            findViewById<TextView>(R.id.dangki).setOnClickListener {
                val intent = Intent(this, SigUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}