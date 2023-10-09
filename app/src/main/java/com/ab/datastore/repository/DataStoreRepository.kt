package com.ab.datastore.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ab.datastore.repository.DataStoreKeys.STRING_DATA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

object DataStoreKeys{
    val STRING_DATA = stringPreferencesKey("string_data")

}

class DataStoreRepository (context: Context){



    suspend fun saveDataToPreferencesStore(key : Preferences.Key<String>, value: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }


    fun getPreferenceFlow(context: Context, key: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data
            .catch { e ->
                if (e is IOException) {
                    e.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { preferences ->
                preferences[key] ?: "None"
            }
    }

}


