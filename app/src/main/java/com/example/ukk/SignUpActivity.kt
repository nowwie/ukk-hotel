package com.example.ukk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ukk.user.UserModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var tilUsername: TextInputLayout
    private lateinit var edtUsername: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText
    private lateinit var tilCnfPwd: TextInputLayout
    private lateinit var edtCnfPwd: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var pbLoading: ProgressBar
    private lateinit var rgRole: RadioGroup
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Inisialisasi Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("user")

        tilUsername = findViewById(R.id.tilUsername)
        edtUsername = findViewById(R.id.edtUsername)
        tilPassword = findViewById(R.id.tilPassword)
        edtPassword = findViewById(R.id.edtPassword)
        tilCnfPwd = findViewById(R.id.tilCnfPwd)
        edtCnfPwd = findViewById(R.id.edtCnfPwd)
        btnRegister = findViewById(R.id.btnRegister)
        pbLoading = findViewById(R.id.pbLoading)
        rgRole = findViewById(R.id.rgRole)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener(View.OnClickListener {
            registerUser()
        })

        tvLogin.setOnClickListener(View.OnClickListener {
            // Tambahkan kode untuk mengarahkan pengguna ke halaman registrasi
            startActivity(Intent(this, LoginActivity::class.java))
        })
    }

    private fun registerUser() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val cnfPassword = edtCnfPwd.text.toString().trim()

        // Validasi input pengguna
        if (username.isEmpty() || password.isEmpty() || cnfPassword.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != cnfPassword) {
            Toast.makeText(this, "Password dan konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
            return
        }

        // Menampilkan ProgressBar selama proses registrasi
        pbLoading.visibility = View.VISIBLE

        // Melakukan registrasi dengan Firebase Authentication
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                // Menyembunyikan ProgressBar setelah registrasi selesai
                pbLoading.visibility = View.GONE
                if (task.isSuccessful) {
                    // Registrasi berhasil

                    // Mendapatkan UID pengguna yang baru terdaftar
                    val userId = auth.currentUser?.uid

                    // Mendapatkan nilai dari RadioButton yang dipilih
                    val selectedRoleId = rgRole.checkedRadioButtonId
                    val selectedRole = when (selectedRoleId) {
                        R.id.rbAdmin -> "Admin"

                        R.id.rbUser -> "Kasir"
                        else -> ""
                    }

                    // Membuat objek pengguna
                    val user = UserModel(username, selectedRole)

                    // Menyimpan data pengguna ke Firebase Realtime Database
                    if (userId != null) {
                        usersRef.child(userId).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                // Tambahkan kode untuk mengarahkan pengguna ke halaman utama aplikasi jika diperlukan
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Registrasi gagal
                    Toast.makeText(this, "Registrasi gagal. Periksa kembali email dan password Anda.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

