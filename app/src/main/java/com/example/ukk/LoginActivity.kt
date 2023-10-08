package com.example.ukk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ukk.user.UserModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var tilUsername: TextInputLayout
    private lateinit var edtUsername: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("user")

        tilUsername = findViewById(R.id.tilUsername)
        edtUsername = findViewById(R.id.edtUsername)
        tilPassword = findViewById(R.id.tilPassword)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        pbLoading = findViewById(R.id.pbLoading)

        btnLogin.setOnClickListener(View.OnClickListener {
            loginUser()
        })

        tvRegister.setOnClickListener(View.OnClickListener {
            // kode untuk mengarahkan pengguna ke halaman registrasi
            startActivity(Intent(this, SignUpActivity::class.java))
        })
    }

    private fun loginUser() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            return
        }

        // Menampilkan ProgressBar selama proses login
        pbLoading.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                // Menyembunyikan ProgressBar setelah login selesai
                pbLoading.visibility = View.GONE
                if (task.isSuccessful) {
                    // Login berhasil
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // untuk ke halaman utama aplikasi dan simpan informasi login agar pengguna tetap masuk
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                    // Misalnya, jika Anda ingin mengambil data pengguna dari Firebase Realtime Database:
                    val userUid = auth.currentUser?.uid
                    userUid?.let {
                        usersRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val userData = snapshot.getValue(UserModel::class.java)
                                    if (userData != null) {
                                        val username = userData.eUsername
                                        val email = userData.email
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle kesalahan jika diperlukan
                            }
                        })
                    }
                } else {
                    // Login gagal
                    Toast.makeText(this, "Login gagal. Periksa kembali email dan password Anda.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
