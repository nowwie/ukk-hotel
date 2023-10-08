package com.example.ukk.Transaksi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.Adapter.TransaksiAdapter
import com.example.ukk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class TransaksiActivity : AppCompatActivity() {

    private lateinit var btnInsertPesan: FloatingActionButton
    private lateinit var pRecyclerView: RecyclerView
    private lateinit var pList: ArrayList<TransaksiModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBack: ImageButton
    private lateinit var btndel: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        btnInsertPesan = findViewById(R.id.btnAddTransaksi)

        btnInsertPesan.setOnClickListener{
            val intent = Intent(this, TransaksiInsertActivity::class.java)
            startActivity(intent)
        }

        pRecyclerView = findViewById(R.id.rvTransaksi)
        pRecyclerView.layoutManager = LinearLayoutManager(this)
        pRecyclerView.setHasFixedSize(true)

        pList = arrayListOf<TransaksiModel>()

        getPesanan()
    }

    private fun getPesanan() {
        dbRef = FirebaseDatabase.getInstance().getReference("Transaksi")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pList.clear()
                if (snapshot.exists()) {
                    for (eSnap in snapshot.children) {
                        val pData = eSnap.getValue(TransaksiModel::class.java)
                        pList.add(pData!!)
                    }
                    val pAdapter = TransaksiAdapter(pList)
                    pRecyclerView.adapter = pAdapter

                    pAdapter.setOnItemclickListener(object : TransaksiAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent =
                                Intent(this@TransaksiActivity, TransaksiDetailsActivity::class.java)

                            intent.putExtra("idPesan", pList[position].IdPesan)
                            intent.putExtra("namaPesan", pList[position].namaPesan)
                            intent.putExtra("tglCheckIn", pList[position].tglCheckIn)
                            intent.putExtra("tglCheckOut", pList[position].tglCheckOut)
                            intent.putExtra("harga", pList[position].harga)
                            startActivity(intent)
                        }
                    })


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

//        btndel = findViewById(R.id.hapus)
//        btndel.setOnClickListener {
//            hapusData(
//                intent.getStringExtra("idPesan").toString()
//            )
        }
    }

//    private fun hapusData(id: String){
//        val dbRef = FirebaseDatabase.getInstance().getReference("Transaksi").child(id)
//        val pTask = dbRef.removeValue()
//
//        pTask.addOnSuccessListener {
//            Toast.makeText(this, "Book Deleted", Toast.LENGTH_LONG).show()
//
//            val intent = Intent(this, TransaksiActivity::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener { error ->
//            Toast.makeText(this, "Deelete Err ${error.message}", Toast.LENGTH_LONG).show()
//        }
//    }
