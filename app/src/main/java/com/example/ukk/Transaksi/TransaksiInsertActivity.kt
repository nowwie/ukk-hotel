package com.example.ukk.Transaksi


import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.ukk.R
import com.example.ukk.TipeKamar.TipeModel
import com.example.ukk.Transaksi.TransaksiModel
import com.google.firebase.database.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_insert)

        nmPesan = findViewById(R.id.txtNama)
        tglChekIn = findViewById(R.id.idCheckIn)
        tglChekOut = findViewById(R.id.idCheckOut)
        spinner = findViewById<Spinner>(R.id.spjKamar)
        jmKamar = findViewById(R.id.jmlKmr)
        pharga = findViewById(R.id.idHarga)

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
        val sharga = pharga.text.toString()
        val selectedKamar = spinner.selectedItem.toString()

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
        tglChekOut.error = "Masukkan Tanggal Check Out"
        return
        if (tglco.isEmpty()) {
        }
        if (selectedKamar == "Pilih Tipe Kamar") {
            Toast.makeText(this, "Pilih Tipe Kamar", Toast.LENGTH_LONG).show()
            return
        }

        val IdPesan = dbRef.push().key!!
        val pesan = TransaksiModel(IdPesan, nPesan, tglck, tglco,  jmlKamar)
        Toast.makeText(this, "Nama Kamar: $selectedKamar", Toast.LENGTH_LONG).show()

        dbRef.child(IdPesan).setValue(pesan)
            .addOnCompleteListener {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()

                nmPesan.text.clear()
                jmKamar.text.clear()
                tglChekIn.text.clear()
                spinner.setSelection(0)
                tglChekOut.text.clear()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

}
