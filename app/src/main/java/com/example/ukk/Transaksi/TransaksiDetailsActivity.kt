package com.example.ukk.Transaksi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.ukk.R
import org.w3c.dom.Text

class TransaksiDetailsActivity : AppCompatActivity() {

    private lateinit var tvTipeId: TextView
    private lateinit var tvNamaPesan: TextView
    private lateinit var tvJmlKamar: TextView
    private lateinit var tvHarga: TextView
    private lateinit var btnUpdateUser: Button
    private lateinit var btnDeleteUser: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_details)
    }
}