package Adapter


import Model.MenuItem
import ViewModel.LearningActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ViewModel.QuanLyTaiKhoanActivity
import ViewModel.QuestionActivity
import android.util.Log
import com.example.duan.R

class MenuAdapter(val context :Context, val MenuList:ArrayList<MenuItem>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val viewItem=inflater.inflate(R.layout.menuitem,parent,false)
        return ViewHolder(viewItem)
    }

    override fun getItemCount(): Int {
        return MenuList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("kq", itemCount.toString())
        if(MenuList[position].text!=null)
        holder.textView.text=  MenuList[position].text
        holder.imageView.setImageResource(MenuList[position].imageId)
        holder.itemView.setOnClickListener {
            // Thực hiện hành động tương ứng với item được nhấn
            if (position==0)
            {
                val intent = Intent(context, LearningActivity::class.java)
                context.startActivity(intent)
            }
            if (position==1)
                Toast.makeText(context, "Biển báo đường bộ", Toast.LENGTH_SHORT).show()
            if (position==2)
            {
                val intent = Intent(context, QuestionActivity::class.java)
                context.startActivity(intent)
            }
            if (position==3)
            {
                val intent = Intent(context, QuanLyTaiKhoanActivity::class.java)
                context.startActivity(intent)
//                context.finish()
            }
        }

    }

    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.item_image)
        val textView = itemView.findViewById<TextView>(R.id.item_text)
    }
}