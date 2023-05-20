package ViewModel

import Adapter.MenuAdapter
import Model.MenuItem
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duan.R

class
HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val items = ArrayList<MenuItem>()
        items.add(  MenuItem(R.drawable.menu1, "Học lý thuyết"))
        items.add(  MenuItem(R.drawable.menu1, "Biển báo đường bộ"))
        items.add(  MenuItem(R.drawable.menu1, "Làm đề"))
        items.add(  MenuItem(R.drawable.menu1, "Quản lý tài khoản"))
        val listView = findViewById<RecyclerView>(R.id.countryview)
        val adapter = MenuAdapter(this, items)
        listView.adapter = adapter
        listView.layoutManager= GridLayoutManager(this,1)
    }
}
