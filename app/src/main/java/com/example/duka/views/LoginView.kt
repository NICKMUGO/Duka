package com.example.duka.views

import android.app.Application
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.duka.data.database.DukaDatabase
import com.example.duka.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Create DataStore inside this file
private val Application.dataStore by preferencesDataStore("user_prefs")

class LoginView(app: Application) : AndroidViewModel(app) {

    private val userDao = DukaDatabase.getDatabase(app).userDao()

    // DataStore key
    private val USER_ID = intPreferencesKey("user_id")

    // Save user id globally
    private suspend fun saveUserId(id: Int) {
        getApplication<Application>().dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    // FUNCTION 1: LOGIN USER
    fun loginUser(email: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.login(email, password)

            if (user != null) {
                saveUserId(user.id) // save globally
            }

            callback(user)
        }
    }

    // FUNCTION 2: ACCESS USER ID ANYWHERE
    fun getUserId(): Flow<Int?> {
        return getApplication<Application>().dataStore.data.map { prefs ->
            prefs[USER_ID]
        }
    }
}
