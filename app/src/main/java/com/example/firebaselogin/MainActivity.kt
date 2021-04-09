package com.example.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    lateinit var nameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var signUpTV: TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        firebaseAuth = FirebaseAuth.getInstance()

        nameEditText = findViewById(R.id.editTextUserName)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)
        signUpTV = findViewById(R.id.signUpButton)

        authStateListener = AuthStateListener {
            val mFirebaseUser: FirebaseUser? = firebaseAuth.currentUser
            if (mFirebaseUser != null) {
                Toast.makeText(this@MainActivity, "You are logged in!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, Profile::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Welcome!", Toast.LENGTH_SHORT).show()
            }
        }

        signUpTV.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email: String = nameEditText.text!!.toString()
            val password: String = passwordEditText.text!!.toString()
            if(TextUtils.isEmpty(email)){
                nameEditText.error = "Please Enter Email Address"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                passwordEditText.error = "Please Enter Password"
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@MainActivity) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity,
                            "Login Error, Please check your credentials and try Logging in Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(this@MainActivity, Profile::class.java)
                        startActivity(intent)
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }
}