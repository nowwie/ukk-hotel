package com.example.ukk.user

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

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserUsername: TextView
    private lateinit var tvUserPassword: TextView
    private lateinit var btnUpdateUser: Button
    private lateinit var btnDeleteUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        initView()
        setValuesToViews()

        btnUpdateUser.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("userId").toString(),
                intent.getStringExtra("userName").toString()
            )
        }

        btnDeleteUser.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("userId").toString()
            )
        }
    }

    private fun initView() {
        tvUserId = findViewById(R.id.tvUserId)
        tvUserName = findViewById(R.id.tvUserName)
        tvUserUsername = findViewById(R.id.tvUserUsername)
        tvUserPassword = findViewById(R.id.tvUserPassword)

        btnUpdateUser = findViewById(R.id.btnUpdateUser)
        btnDeleteUser = findViewById(R.id.btnDeleteUser)
    }

    private fun setValuesToViews() {
        tvUserId.text = intent.getStringExtra("userId")
        tvUserName.text = intent.getStringExtra("userName")
        tvUserUsername.text = intent.getStringExtra("userUsername")
        tvUserPassword.text = intent.getStringExtra("userPassword")
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("user").child(id)
        val uTask = dbRef.removeValue()

        uTask.addOnSuccessListener {
            Toast.makeText(this, "User data deleted",Toast.LENGTH_LONG).show()

            val intent = Intent(this, UserActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        userId: String,
        userName: String
    ){
        val uDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val uDialogView = inflater.inflate(R.layout.user_update, null)

        uDialog.setView(uDialogView)

        val etUserName = uDialogView.findViewById<EditText>(R.id.etUserName)
        val etUserUsername = uDialogView.findViewById<EditText>(R.id.etUserUsername)
        val etUserPassword = uDialogView.findViewById<EditText>(R.id.etUserPassword)

        val btnUpdateDataUser = uDialogView.findViewById<Button>(R.id.btnUpdateDataUserDialog)

        etUserName.setText(intent.getStringExtra("userName").toString())
        etUserUsername.setText(intent.getStringExtra("userUsername").toString())
        etUserPassword.setText(intent.getStringExtra("userPassword").toString())

        uDialog.setTitle("Updating $userName Record")

        val alertDialog = uDialog.create()
        alertDialog.show()

        btnUpdateDataUser.setOnClickListener {
            updateUserData(
                userId,
                etUserName.text.toString(),
                etUserUsername.text.toString(),
                etUserPassword.text.toString()
            )

            Toast.makeText(applicationContext, "User Data Updated", Toast.LENGTH_LONG).show()

            tvUserName.text = etUserName.text.toString()
            tvUserUsername.text = etUserUsername.text.toString()
            tvUserPassword.text = etUserPassword.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateUserData(
        userId: String,
        userName: String,
        userUsername: String,
        userPassword: String
    ){
        val dbref = FirebaseDatabase.getInstance().getReference("user").child(userId)
        val userInfo = UserModel(userId, userName, userUsername, userPassword)
        dbref.setValue(userInfo)
    }
}