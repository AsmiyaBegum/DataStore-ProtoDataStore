package com.ab.datastore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ab.datastore.repository.DataStoreKeys
import com.ab.datastore.repository.DataStoreRepository
import com.ab.datastore.repository.ProtoRepository
import com.ab.datastore.repository.protoDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)
    private val protoRepository = ProtoRepository(application.applicationContext.protoDataStore)

    val readFromDataStore = repository.getPreferenceFlow(application.applicationContext,
        DataStoreKeys.STRING_DATA)

    fun saveToDataStore(myName: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveDataToPreferencesStore(DataStoreKeys.STRING_DATA,myName,getApplication<Application>().applicationContext)
    }

    val readFromProtoDataStore = protoRepository.readProto

    fun saveToProtoDataStore(name : String, age : Int, isCompleted : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        protoRepository.updateValue(name, age, isCompleted)
    }
}