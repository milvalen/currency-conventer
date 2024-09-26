package org.example.currency_converter.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
data class ApiResponse(val meta: ApiMetaData, val data: Map<String, CurrencyObject>)

@Serializable
data class ApiMetaData(val last_updated_at: String)

@Serializable
open class CurrencyObject: RealmObject {
    companion object

    @PrimaryKey
    var docID = ObjectId()

    var code = ""
    var value = 0.0
}
