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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class   TransaksiActivity : AppCompatActivity() {

    private lateinit var btnInsertPesan: FloatingActionButton
    private lateinit var pRecyclerView: RecyclerView
    private lateinit var pList: ArrayList<TransaksiModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBack: ImageButton
    private lateinit var btndel: ImageButton
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        btnInsertPesan = findViewById(R.id.btnAddTransaksi)

        btnInsertPesan.setOnClickListener {
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
        val docRef = db.collection("Transaksi")
        docRef.addSnapshotListener(){
            snapshot, e ->
            if (e != null){
                Toast.makeText(this, "Listen failed.", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            if (snapshot != null){
                pList.clear()
                for (document in snapshot){
                    val pesanan = document.toObject(TransaksiModel::class.java)
                    pList.add(pesanan)
                }
                val TAdapter = TransaksiAdapter(pList)
                pRecyclerView.adapter = TAdapter

                TAdapter.setOnItemclickListener(object : TransaksiAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@TransaksiActivity, TransaksiDetailsActivity::class.java)
                        intent.putExtra("id", pList[position].IdPesan)
                        intent.putExtra("nama", pList[position].namaPesan)
                        intent.putExtra("jmlKmr", pList[position].jmlKmr)
                        intent.putExtra("tipeKmr", pList[position].selectedItem)
                        intent.putExtra("tglChekIn", pList[position].tglCheckIn)
                        intent.putExtra("tglChekOut", pList[position].tglCheckOut)
                        intent.putExtra("harga", pList[position].harga)
                        startActivity(intent)
                    }
                })
            }

        }
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
