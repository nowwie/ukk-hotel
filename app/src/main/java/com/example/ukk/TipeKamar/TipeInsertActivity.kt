package com.example.ukk.TipeKamar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.ukk.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class TipeInsertActivity : AppCompatActivity() {

    private lateinit var tName: EditText
    private lateinit var tHarga: EditText
    private lateinit var tDeskripsi: EditText
    private lateinit var btnSaveTipe: Button
    private lateinit var btnBack: ImageButton
    private lateinit var dbRef: DatabaseReference
    private lateinit var frdb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe_insert)

        tName = findViewById(R.id.edtNamaTipe)
        tHarga = findViewById(R.id.edtHarga)
        tDeskripsi = findViewById(R.id.edtDeskripsi)
        btnSaveTipe = findViewById(R.id.btn_save_tipe)
        btnBack = findViewById(R.id.backAddTipe)

        dbRef = FirebaseDatabase.getInstance().getReference("Tipe")

        btnSaveTipe.setOnClickListener{
            saveTipeData()
        }
        btnBack.setOnClickListener{ startActivity(Intent(this,TipeActivity::class.java))}
    }

    private fun saveTipeData(){
        //getting value
        val pName = tName.text.toString()
        val pHarga = tHarga.text.toString()
        val pDeskripsi = tDeskripsi.text.toString()

        if(pName.isEmpty()){
            tName.error = "Please enter Name"
        }
        if(pHarga.isEmpty()){
            tHarga.error = "Masukkan Harga"
        }
        if(pDeskripsi.isEmpty()){
            tDeskripsi.error = "Masukkan Deskripsi Singkat"
        }



//        val tipe = TipeModel( tipeId, pName, pHarga, pDeskripsi)

        frdb = FirebaseFirestore.getInstance()
        //tipeId berdasarkan documentId
        val data = hashMapOf(
            "NamaKmr" to pName,
            "Harga" to pHarga,
            "deskripsi" to pDeskripsi
        )
        frdb.collection("Tipe")
            .add(data)
            .addOnSuccessListener{ documentReference ->
                Log.d("TAG", "DocumentSnapshot successfully written!")
                val intent = Intent(this, TipeActivity::class.java)
                startActivity(intent)
                val tipeId = documentReference.id
                frdb.collection("Tipe").document(tipeId)
                    .update("TipeId", tipeId)
                    .addOnSuccessListener {
                        Log.d("TAG", "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener{
                        Log.w("TAG", "Error updating document", it)
                    }
            }
            .addOnFailureListener{
                Log.w("TAG", "Error writing document", it)
            }
    }
}