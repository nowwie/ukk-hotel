package com.example.ukk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.R
import com.example.ukk.user.UserModel
import java.lang.reflect.Type
import java.text.FieldPosition

class UserAdapter ( private val userList: ArrayList<UserModel> )
    : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var uListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemclickListener(clickListener: onItemClickListener){
        uListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(itemView, uListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentUser = userList[position]
        holder.tvUserName.text = currentUser.eUsername
    }

    override fun getItemCount():Int{
        return userList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)

        init{
            itemView.setOnClickListener({
                clickListener.onItemClick(adapterPosition)
            })
        }
    }
}