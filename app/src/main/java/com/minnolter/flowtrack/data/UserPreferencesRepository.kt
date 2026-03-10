package com.minnolter.flowtrack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val USER_POINTS = intPreferencesKey("user_points")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: true
        }

    val userPoints: Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.USER_POINTS] ?: 0
        }

    suspend fun saveThemePreference(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }

    suspend fun saveUserPoints(points: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_POINTS] = points
        }
    }
}

/*
To add a new piece of data to your UserPreferencesRepository (like a "User Name" or "Last Sync Time"), follow these three steps:
1. Add a Key
Inside the private object PreferencesKeys, define a new key using the appropriate type (e.g., stringPreferencesKey, longPreferencesKey, etc.).
kotlin
private object PreferencesKeys {
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    val USER_POINTS = intPreferencesKey("user_points")
    // ADD THIS:
    val USER_NAME = stringPreferencesKey("user_name")
}
Use code with caution.

2. Create a Flow to Read it
Create a new val that maps the data store to your new key. Provide a default value in case the key doesn't exist yet.
kotlin
val userName: Flow<String> = context.dataStore.data
    .catch { exception ->
        if (exception is IOException) emit(emptyPreferences()) else throw exception
    }
    .map { preferences ->
        // Default to "Guest" if nothing is saved
        preferences[PreferencesKeys.USER_NAME] ?: "Guest"
    }
Use code with caution.

3. Add a Suspend Function to Save it
Create a function to update the value. This function must be suspend because DataStore.edit performs disk I/O.
kotlin
suspend fun saveUserName(name: String) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.USER_NAME] = name
    }
}
 */