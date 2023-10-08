package com.example.ukk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.R
import com.example.ukk.Transaksi.TransaksiModel
import java.text.FieldPosition

class TransaksiAdapter (private val transaksiList: ArrayList<TransaksiModel>):
        RecyclerView.Adapter<TransaksiAdapter.ViewHolder>(){
            private lateinit var pListener: onItemClickListener

            interface onItemClickListener{
                fun onItemClick(position: Int)
            }
    fun setOnItemclickListener(clickListener:onItemClickListener){
        pListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaksi_list, parent,false)
        return TransaksiAdapter.ViewHolder(itemView, pListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentPesan = transaksiList[position]
        holder.tvName.text = currentPesan.namaPesan
        holder.tvChekin.text = currentPesan.tglCheckIn
        holder.tvCo.text = currentPesan.tglCheckOut
        holder.tvHarga.text = currentPesan.harga
    }

    override fun getItemCount():Int{
        return transaksiList.size
    }

    class ViewHolder(itemView: View, clickListener: TransaksiAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.tvNameK)
        val tvChekin: TextView = itemView.findViewById(R.id.tvCheckIn)
        val tvCo: TextView = itemView.findViewById(R.id.tvCheckOut)
        val tvHarga: TextView = itemView.findViewById(R.id.tvTotal)

        init{
            itemView.setOnClickListener({
                clickListener.onItemClick(adapterPosition)
            })
        }
    }
        }