package ViewModel

import DAO.UserDao
import Model.user
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.duan.R
import com.google.gson.Gson

class QuanLyTaiKhoanActivity : AppCompatActivity() {
    lateinit var  id :TextView
    lateinit var username :TextView
    lateinit var password :TextView
    lateinit var name :TextView
    lateinit var sharedPreferences : SharedPreferences
    lateinit var u : user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_tai_khoan)
        khoitao()
// Gán dữ liệu người dùng vào các TextView
        username.text = u.username
        password.text = u.password
        name.text = u.name
        id.text = u.id.toString()
        findViewById<Button>(R.id.save_button).setOnClickListener {
             id.text =id.text.toString()
             username.text = findViewById<TextView>(R.id.username_edittext).text.toString()
             password.text = findViewById<TextView>(R.id.password_edittext).text.toString()
             name.text = findViewById<TextView>(R.id.name_edittext).text.toString()
            val editor = sharedPreferences.edit()
            val dao = UserDao(this)
            dao.updateUser(id.text.toString().toInt(),username.text.toString(),password.text.toString(),name.text.toString())
            u = dao.checkLogin(username.text.toString(),password.text.toString())!!
            if(u!=null){
                val gson = Gson()
                val userString=gson.toJson(u)
                editor.putString("user",userString)
                editor.apply()
                Toast.makeText(this, "Cập nhập thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cập nhập thất bại", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun khoitao(){
         id = findViewById<TextView>(R.id.id)
         username = findViewById<TextView>(R.id.username_edittext)
         password = findViewById<TextView>(R.id.password_edittext)
         name = findViewById<TextView>(R.id.name_edittext)
         sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val gson = Gson()
        val userJsonString = sharedPreferences.getString("user", null)
        if (userJsonString != null) {
            u = gson.fromJson(userJsonString, user::class.java)
        }
    }
}