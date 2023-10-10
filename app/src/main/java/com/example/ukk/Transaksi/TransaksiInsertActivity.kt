package com.example.ukk.Transaksi


import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ukk.R
import com.example.ukk.TipeKamar.TipeModel
import com.example.ukk.Transaksi.TransaksiModel
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TransaksiInsertActivity : AppCompatActivity() {

    private lateinit var nmPesan: EditText
    private lateinit var jmKamar: EditText
    private lateinit var spinner: Spinner
    private lateinit var tglChekIn: EditText
    private lateinit var tglChekOut: EditText
    private lateinit var pharga: TextView
    private lateinit var btnPesan: Button

    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var tipeKamarList: List<TipeModel>
    private lateinit var db :FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_insert)

        nmPesan = findViewById(R.id.txtNama)
        tglChekIn = findViewById(R.id.idCheckIn)
        tglChekOut = findViewById(R.id.idCheckOut)
        spinner = findViewById<Spinner>(R.id.spjKamar)
        jmKamar = findViewById(R.id.jmlKmr)
        pharga = findViewById(R.id.idHarga)
        db = FirebaseFirestore.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Transaksi")

        // Tambahkan listener untuk idCheckIn
        tglChekIn.setOnClickListener {
            showDatePickerDialog(tglChekIn)
        }

        // Tambahkan listener untuk idCheckOut
        tglChekOut.setOnClickListener {
            showDatePickerDialog(tglChekOut)
        }

        btnPesan = findViewById(R.id.pesanHotel)
        btnPesan.setOnClickListener {
            savePesanan()
        }

        setSpinner()
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Tindakan yang akan dilakukan saat tanggal dipilih
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(selectedDate)
            },
            year, month, dayOfMonth
        )

        datePickerDialog.show()
    }


    private fun savePesanan() {
        val nPesan = nmPesan.text.toString()
        val jmlKamar = jmKamar.text.toString()
        val tglck = tglChekIn.text.toString()
        val tglco = tglChekOut.text.toString()
        val tipeKamar = spinner.selectedItem.toString()
        val harga = pharga.text.toString()

        if (nPesan.isEmpty()) {
            nmPesan.error = "Isi Nama Anda"
            return
        }
        if (jmlKamar.isEmpty()) {
            jmKamar.error = "Isi Jumlah Kamar"
            return
        }
        if (tglck.isEmpty()) {
            tglChekIn.error = "Masukkan Tanggal Check In"
            return
        }

        val pesan = hashMapOf(
            "namaPesan" to nPesan,
            "jmlKmr" to jmlKamar,
            "tglCheckIn" to tglck,
            "tglCheckOut" to tglco,
            "selectedItem" to tipeKamar,
            "harga" to harga
        )
        val docRef = db.collection("Transaksi")
        docRef.add(pesan)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data Gagal Disimpan", Toast.LENGTH_SHORT).show()
            }

    }

    private fun setSpinner() {
        db.collection("Tipe")
            .get()
            .addOnSuccessListener { result ->
                tipeKamarList = result.toObjects(TipeModel::class.java)
                val tipeKamarNameList = tipeKamarList.map { it.NamaKmr }
                adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,)
                spinner.adapter = adapter
                val placeholder = "Pilih Tipe Kamar"
                adapter.add(placeholder)
                adapter.addAll(tipeKamarNameList)
                adapter.notifyDataSetChanged()

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (position == 0) {
                            pharga.text = ""
                        } else {
                            val selectedTipeKamar = tipeKamarList[position - 1]
                            val selectedKamar = spinner.selectedItem.toString()
                            val jumlahKamar = jmKamar.text.toString()
                            //add onTextChangedListener to edittext
                            Toast.makeText(this@TransaksiInsertActivity, selectedKamar, Toast.LENGTH_SHORT).show()
                            if (jumlahKamar.isEmpty()) {
                                jmKamar.error = "Masukkan Jumlah Kamar"
                                return
                            }else{
                                db.collection("Tipe").whereEqualTo("NamaKmr", selectedKamar)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        val hargaKamar = result.documents[0].get("Harga").toString()
                                        Toast.makeText(this@TransaksiInsertActivity, hargaKamar, Toast.LENGTH_SHORT).show()
                                        Log.d(TAG, hargaKamar)
                                        val totalHarga = hargaKamar.toInt() * jumlahKamar.toInt()
                                        pharga.text = totalHarga.toString()
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e(TAG, "get failed with ", exception)
                                        Toast.makeText(this@TransaksiInsertActivity, "Error Fetching Data", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }

            .addOnFailureListener { exception ->
                Log.e("Firestore Error", exception.message.toString())
            }
    }

}
