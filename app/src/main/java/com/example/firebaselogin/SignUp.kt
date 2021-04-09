package com.example.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SignUp : AppCompatActivity() {

    lateinit var nameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var passwordReEnterEditText: EditText
    lateinit var hobbiesEditText: EditText
    lateinit var signUp: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: DatabaseReference
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firebaseDatabase = database?.reference!!.child("Users")

        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextPasswordSignup)
        passwordReEnterEditText = findViewById(R.id.editTextPasswordSignup2)
        hobbiesEditText = findViewById(R.id.editTextHobbies)
        signUp = findViewById(R.id.button)

        signUp.setOnClickListener {
            val email: String = emailEditText.text!!.toString()
            val password: String = passwordEditText.text!!.toString()
            val passwordRe: String = passwordReEnterEditText.text.toString()
            val name: String = nameEditText.text!!.toString()
            val hobbies: String = hobbiesEditText.text.toString()
            if(TextUtils.isEmpty(name)){
                nameEditText.error = "Please Enter Name"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(email)){
                emailEditText.error = "Please Enter Email Address"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                passwordEditText.error = "Please Enter Password"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(passwordRe)){
                passwordReEnterEditText.error = "Please Re-Enter Password"
                return@setOnClickListener
            }
            if (passwordEditText.text.length > 5) {
                if (password == passwordRe) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@SignUp) { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this@SignUp,
                                    "SignUp Unsuccessful, Please Try Again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val currentUser = firebaseAuth.currentUser
                                val currentUserDb = firebaseDatabase.child((currentUser.uid))
                                currentUserDb.child("username").setValue(name)
                                currentUserDb.child("email").setValue(email)
                                currentUserDb.child("hobbies").setValue(hobbies)
                                Toast.makeText(this, "User saved!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignUp, MainActivity::class.java))
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "The passwords don't match, please try again!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    passwordEditText.requestFocus()
                }
            } else {
                Toast.makeText(
                    this@SignUp,
                    "The password should have a minimum number of 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
                passwordEditText.requestFocus()
            }
        }
    }
}

