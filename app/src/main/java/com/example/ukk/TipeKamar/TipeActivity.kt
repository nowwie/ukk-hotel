package com.example.ukk.TipeKamar

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.Adapter.TipeAdapter
import com.example.ukk.MainActivity
import com.example.ukk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class TipeActivity : AppCompatActivity() {

    private lateinit var btnInsertKmr: FloatingActionButton
    private lateinit var kRecyclerView: RecyclerView
    private lateinit var kList: ArrayList<TipeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBackT: ImageButton

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

    private fun getTipe(){
        dbRef = FirebaseDatabase.getInstance().getReference("Tipe")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                kList.clear()
                if(snapshot.exists()){
                    for(eSnap in snapshot.children){
                        val kData = eSnap.getValue(TipeModel::class.java)
                        kList.add(kData!!)
                    }
                    val kAdapter = TipeAdapter(kList)
                    kRecyclerView.adapter = kAdapter

                    kAdapter.setOnItemclickListener(object : TipeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@TipeActivity, TipeDetailsActivity::class.java)

                            intent.putExtra("tipeId", kList[position].TipeId)
                            intent.putExtra("namaKmr", kList[position].NamaKmr)
                            intent.putExtra("deskripsi", kList[position].deskripsi)
                            intent.putExtra("harga", kList[position].Harga)
                            startActivity(intent)
                        }
                    })
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}