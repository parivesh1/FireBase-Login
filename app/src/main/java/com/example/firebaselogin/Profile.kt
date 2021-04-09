package com.example.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: DatabaseReference
    private var database: FirebaseDatabase? = null

    lateinit var welcome: TextView
    lateinit var userName: TextView
    lateinit var emailAddress: TextView
    lateinit var hobbies: TextView
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        welcome = findViewById(R.id.ProfileName)
        userName = findViewById(R.id.Name)
        emailAddress = findViewById(R.id.name)
        hobbies = findViewById(R.id.name2)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firebaseDatabase = database?.reference!!.child("Users")
        loadProfile()

        logout = findViewById(R.id.logoutButton)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@Profile, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfile() {
        val user = firebaseAuth.currentUser
        val userReference = firebaseDatabase.child(user.uid)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username: String = snapshot.child("username").value!!.toString()
                welcome.text = "Welcome, $username!!"
                userName.text = username
                emailAddress.text = snapshot.child("email").value!!.toString()
                val h = snapshot.child("hobbies").value!!.toString()
                if (h.isNotEmpty()){
                    hobbies.text = h
                }
                else {
                    hobbies.text = "No Hobbies!"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
            }

        })
    }
}