package org.example.currency_converter.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import org.example.currency_converter.domain.MongoDbRepo
import org.example.currency_converter.domain.model.CurrencyObject
import org.example.currency_converter.domain.model.RequestCondition

class MongoDbImpl: MongoDbRepo {
    private var realm: Realm? = null
    init { setRealm() }

    override fun readCurrencyAmountData(): Flow<RequestCondition<List<CurrencyObject>>> {
        return realm?.query<CurrencyObject>()?.asFlow()?.map {
            result -> RequestCondition.SuccessCondition(result.list)
        } ?: flow { RequestCondition.ErrorCondition("Realm Setting is not Successful") }
    }

    override suspend fun addCurrencyAmountData(currency: CurrencyObject) {
        realm?.write { copyToRealm(currency) }
    }

    override suspend fun cleanDB() { realm?.write { delete(this.query<CurrencyObject>()) } }

    override fun setRealm() {
        if (realm == null || realm!!.isClosed())
            realm = Realm.open(
                RealmConfiguration.Builder(setOf(CurrencyObject::class)).compactOnLaunch().build()
            )
    }
}