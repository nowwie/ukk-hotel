package com.example.ukk.TipeKamar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ukk.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.crypto.spec.DESKeySpec

class TipeDetailsActivity : AppCompatActivity() {

    private lateinit var tvTipeId: TextView
    private lateinit var tvNamaKmr: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvDesk: TextView
    private lateinit var btnUpdateTipe: Button
    private lateinit var btnDelTipe: Button
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipe_details)
        initView()
        setValuesToViews()

        btnUpdateTipe.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("tipeId").toString(),
                intent.getStringExtra("namaKmr").toString()
            )
        }

        btnDelTipe.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("tipeId").toString()
            )
        }
    }

    private fun initView(){
        tvTipeId = findViewById(R.id.tvTipeId)
        tvNamaKmr = findViewById(R.id.tvNmkmr)
        tvHarga = findViewById(R.id.tvHarga)
        tvDesk = findViewById(R.id.tvDeskripsi)

        btnUpdateTipe = findViewById(R.id.btnUpdateTipe)
        btnDelTipe = findViewById(R.id.btnDeleteTipe)

    }

    private fun setValuesToViews(){
        tvTipeId.text = intent.getStringExtra("tipeId")
        tvNamaKmr.text = intent.getStringExtra("namaKmr")
        tvHarga.text = intent.getStringExtra("harga")
        tvDesk.text = intent.getStringExtra("deskripsi")
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Tipe").child(id)
        val kTask = dbRef.removeValue()

        kTask.addOnSuccessListener {
            Toast.makeText(this, "Kamar Data Deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this,TipeActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        TipeId: String,
        NamaKmr: String
    ){
        val uDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val uDialogView = inflater.inflate(R.layout.tipe_update, null)

        uDialog.setView(uDialogView)

        val etNamaKmr = uDialogView.findViewById<EditText>(R.id.etNamaKmr)
        val etDesk = uDialogView.findViewById<EditText>(R.id.etDeskripsi)
        val etHarga = uDialogView.findViewById<EditText>(R.id.etHarga)

        val btnUpdateDataTipe = uDialogView.findViewById<Button>(R.id.btnUpdateDataTipeDialog)

        etNamaKmr.setText(intent.getStringExtra("namaKmr").toString())
        etHarga.setText(intent.getStringExtra("harga").toString())
        etDesk.setText(intent.getStringExtra("deskripsi").toString())

        uDialog.setTitle("Updating $NamaKmr Record")

        val alertDialog = uDialog.create()
        alertDialog.show()

        btnUpdateDataTipe.setOnClickListener{
            updateTipeData(
                TipeId,
                etNamaKmr.text.toString(),
                etHarga.text.toString(),
                etDesk.text.toString()
            )

            Toast.makeText(applicationContext, "Kamar Data Updated", Toast.LENGTH_LONG).show()

            tvNamaKmr.text = etNamaKmr.text.toString()
            tvDesk.text = etDesk.text.toString()
            tvHarga.text = etHarga.text.toString()

            alertDialog.dismiss()

        }
    }

    private fun updateTipeData(tipeId: String, namaKmr: String, harga: String, deskripsi: String, ){
        val docRef = db.collection("Tipe").whereEqualTo("TipeId", tipeId)
        docRef.addSnapshotListener(){
            snapshot, e ->
            if (e != null){
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            if (snapshot != null){
                for (document in snapshot){
                    db.collection("Tipe").document(document.id)
                        .update("NamaKmr", namaKmr)
                    db.collection("Tipe").document(document.id)
                        .update("Harga", harga)
                    db.collection("Tipe").document(document.id)
                        .update("deskripsi", deskripsi)
                }
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}