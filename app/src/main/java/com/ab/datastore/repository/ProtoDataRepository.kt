package com.ab.datastore.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ab.datastore.data.Detail
import com.ab.datastore.data.ProtoDataSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
 val Context.protoDataStore : DataStore<Detail> by dataStore(
    fileName = "proto_detail", serializer = ProtoDataSerializer)



class ProtoRepository(val protoDataStore: DataStore<Detail>) {

    val readProto: Flow<Detail> = protoDataStore.data
        .catch { exception->
            if(exception is IOException){
                Log.d("Error", exception.message.toString())
                emit(Detail.getDefaultInstance())
            }else{
                throw exception
            }
        }

    suspend fun updateValue(name: String, age: Int, isCompleted: Boolean){
        protoDataStore.updateData { preference->
            preference.toBuilder().setName(name).setAge(age).setIsCompleted(isCompleted).build()
        }
    }

}