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

    override fun retrieveCurrencyAmountData(): Flow<RequestCondition<List<CurrencyObject>>> =
        realm?.query<CurrencyObject>()?.asFlow()?.map {
            result -> RequestCondition.SuccessCondition(result.list)
        } ?: flow { RequestCondition.ErrorCondition("Unsuccessful Realm Setting") }

    override suspend fun addCurrencyAmountData(currency: CurrencyObject) {
        realm?.write { copyToRealm(currency) }
    }

    override fun setRealm() {
        if (realm == null || realm!!.isClosed())
            realm = Realm.open(
                RealmConfiguration.Builder(setOf(CurrencyObject::class)).compactOnLaunch().build()
            )
    }
}