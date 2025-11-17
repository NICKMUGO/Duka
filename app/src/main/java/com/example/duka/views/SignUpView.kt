package com.example.duka.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.database.DukaDatabase
import com.example.duka.data.model.User
import kotlinx.coroutines.launch

class SignUpView(app: Application) : AndroidViewModel(app) {

    private val userDao = DukaDatabase.getDatabase(app).userDao()

    fun registerUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                userDao.insertUser(user)
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
