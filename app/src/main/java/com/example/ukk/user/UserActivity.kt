package com.example.ukk.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukk.Adapter.UserAdapter
import com.example.ukk.MainActivity
import com.example.ukk.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class UserActivity : AppCompatActivity() {

    private lateinit var btnInsertUser: FloatingActionButton
//    private lateinit var btnFetchUser: Button
    private lateinit var eRecyclerView: RecyclerView
    private lateinit var eList: ArrayList<UserModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBack: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        btnInsertUser = findViewById(R.id.btnAddUser)

        btnInsertUser.setOnClickListener {
            val intent = Intent(this, UserInsertActivity::class.java)
            startActivity(intent)
        }

        btnBack = findViewById(R.id.backUser)
        btnBack.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }




//        btnFetchUser.setOnClickListener {
//            val intent = Intent(this, UserFetchActivity::class.java)
//            startActivity(intent)
//        }

        eRecyclerView = findViewById(R.id.rvUser)
        eRecyclerView.layoutManager = LinearLayoutManager(this)
        eRecyclerView.setHasFixedSize(true)

        eList = arrayListOf<UserModel>()

        getUser()
    }

    private fun getUser() {
        dbRef = FirebaseDatabase.getInstance().getReference("user")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eList.clear()
                if(snapshot.exists()){
                    for(eSnap in snapshot.children){
                        val uData = eSnap.getValue(UserModel::class.java)
                        eList.add(uData!!)
                    }
                    val uAdapter = UserAdapter(eList)
                    eRecyclerView.adapter = uAdapter

                    uAdapter.setOnItemclickListener(object :UserAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@UserActivity, UserDetailsActivity::class.java)

                            intent.putExtra("userId", eList[position].userId)
                            intent.putExtra("userName", eList[position].eName)
                            intent.putExtra("userUsername", eList[position].eUsername)
                            intent.putExtra("userPassword", eList[position].ePassword)
                            startActivity(intent)
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}