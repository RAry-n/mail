package com.aryan.mail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun fetchUsers() {
        userRepository.fetchUsers { userList ->
            _users.value = userList
        }
    }
}
