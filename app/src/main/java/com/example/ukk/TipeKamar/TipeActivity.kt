package com.example.ukk.TipeKamar

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.Adapter.TipeAdapter
import com.example.ukk.MainActivity
import com.example.ukk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TipeActivity : AppCompatActivity() {

    private lateinit var btnInsertKmr: FloatingActionButton
    private lateinit var kRecyclerView: RecyclerView
    private lateinit var kList: ArrayList<TipeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBackT: ImageButton
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe)

        btnInsertKmr = findViewById(R.id.btnAddTipe)

        btnInsertKmr.setOnClickListener{
            val intent = Intent(this, TipeInsertActivity::class.java)
            startActivity(intent)
        }

        btnBackT = findViewById(R.id.backTipe)
        btnBackT.setOnClickListener{startActivity(Intent(this, MainActivity::class.java))}


        kRecyclerView = findViewById(R.id.rvTipe)
        kRecyclerView.layoutManager = LinearLayoutManager(this)
        kRecyclerView.setHasFixedSize(true)

        kList = arrayListOf<TipeModel>()

        getTipe()
    }

    private fun getTipe() {
        val docRef = db.collection("Tipe")
        docRef.addSnapshotListener(){
            snapshot, e ->
            if (e != null){
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                kList.clear()
                for (document in snapshot){
                    val kData = document.toObject(TipeModel::class.java)
                    kList.add(kData)
                }
                val kAdapter = TipeAdapter(kList)
                kRecyclerView.adapter = kAdapter

                kAdapter.setOnItemclickListener(object : TipeAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@TipeActivity, TipeDetailsActivity::class.java)
                        intent.putExtra("tipeId", kList[position].TipeId)
                        intent.putExtra("namaKmr", kList[position].NamaKmr)
                        intent.putExtra("harga", kList[position].Harga)
                        intent.putExtra("deskripsi", kList[position].deskripsi)
                        startActivity(intent)
                    }
                })
            }else{
                Log.d(TAG, "Current data: null")
            }
        }
    }
}