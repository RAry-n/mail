package com.aryan.mail

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var buttonReceive: Button
    private lateinit var  userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
            editTextName = findViewById(R.id.etText)
            editTextEmail = findViewById(R.id.etEmailAddress)
            buttonSubmit = findViewById(R.id.btnSend)
        buttonReceive = findViewById(R.id.btnReceive)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        buttonSubmit.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please enter both name and email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a User object to store in the database
            val user = User(name, email)

            // Get a reference to the Firebase Database
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")

            // Store the user in the database
            usersRef.push().setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                }
            }
        }
        buttonReceive.setOnClickListener {
            userViewModel.fetchUsers()
        }

        userViewModel.users.observe(this, Observer { users ->
            users.forEach { user ->
                Log.d("MainActivity", "User: ${user.name}, Email: ${user.email}")
            }
        })
    }
}