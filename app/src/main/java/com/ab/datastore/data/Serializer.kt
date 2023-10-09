package com.ab.datastore.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object ProtoDataSerializer : Serializer<Detail> {
    override val defaultValue: Detail = Detail.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): Detail {
        try {
            return Detail.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Detail, output: OutputStream) = t.writeTo(output)
}