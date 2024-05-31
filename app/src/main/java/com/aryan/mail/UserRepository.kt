package com.aryan.mail


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun fetchUsers(callback: (List<User>) -> Unit) {
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }
                callback(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("UserRepository", "Failed to load users", databaseError.toException())
            }
        })
    }
}
