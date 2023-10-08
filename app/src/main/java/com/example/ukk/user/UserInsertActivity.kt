package com.example.ukk.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isEmpty
import com.example.ukk.MainActivity
import com.example.ukk.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserInsertActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinner: Spinner
    private lateinit var btnSaveUser: Button
   private lateinit var btnBack: ImageButton

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_insert)

        etName = findViewById(R.id.edtNamaUser)
        etUsername = findViewById(R.id.edtUsername)
        etPassword = findViewById(R.id.edtPassword)
        spinner = findViewById(R.id.edtJobdesk)
        btnSaveUser = findViewById(R.id.btn_save_user)
        btnBack = findViewById(R.id.backAddUser)

        dbRef = FirebaseDatabase.getInstance().getReference("user")

        btnSaveUser.setOnClickListener {
            saveUserData()
        }
        btnBack.setOnClickListener { startActivity(Intent(this, UserActivity::class.java)) }
    }

    private fun saveUserData(){
        //getting values
        val eName = etName.text.toString()
        val eUsername = etUsername.text.toString()
        val ePass = etPassword.text.toString()
        val eSpinner = spinner

        if(eName.isEmpty()){
            etName.error = "Please enter Name"
        }
        if (eUsername.isEmpty()){
            etUsername.error = "Please enter Username"
        }
        if (ePass.isEmpty()){
            etPassword.error = "Please enter Password"
        }

        val userId = dbRef.push().key!!

        val user = UserModel(userId, eName, eUsername, ePass)

        dbRef.child(userId).setValue(user)
            .addOnCompleteListener{
                Toast.makeText(this, "Succes", Toast.LENGTH_LONG).show()

                etName.text.clear()
                etUsername.text.clear()
                etPassword.text.clear()

            }.addOnFailureListener{ err ->
                Toast.makeText(this, "Error${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}