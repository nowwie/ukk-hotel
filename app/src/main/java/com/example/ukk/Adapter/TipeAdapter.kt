package com.example.ukk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.R
import com.example.ukk.TipeKamar.TipeModel
import java.text.FieldPosition

class TipeAdapter (private val tipeList: ArrayList<TipeModel>): RecyclerView.Adapter<TipeAdapter.ViewHolder>(){
            private lateinit var tListener: onItemClickListener

            interface onItemClickListener{
                fun onItemClick(position: Int)
            }

           fun setOnItemclickListener(clickListener: onItemClickListener){
               tListener = clickListener
           }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tipe_list_item, parent, false)
        return ViewHolder(itemView, tListener ?: throw IllegalStateException("tListener has not been initialized"))
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int){
              val currentTipe = tipeList[position]
              holder.tvTipeNama.text=currentTipe.NamaKmr
              holder.tvTipeDeskripsi.text=currentTipe.deskripsi
              holder.tvTipeHarga.text=currentTipe.Harga
          }

        override fun getItemCount():Int{
            return tipeList.size
        }

    class ViewHolder(itemView: View, clickListener: TipeAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvTipeNama: TextView = itemView.findViewById(R.id.tvNmkmr)
        val tvTipeDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        val tvTipeHarga: TextView = itemView.findViewById(R.id.tvHarga)

        init{
            itemView.setOnClickListener({
                clickListener.onItemClick(adapterPosition)
            })
        }
    }

        }