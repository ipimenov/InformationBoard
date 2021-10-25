package ru.ipimenov.informationboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {

    val database = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        if (auth.uid != null) {
            database
                .child(advertisement.key ?: "empty")
                .child(auth.uid!!)
                .child(AD_NODE)
                .setValue(advertisement)
                .addOnCompleteListener {
                    if (it.isSuccessful) finishWorkListener.onFinish()
                }
        }
    }

    fun advertViewed(advertisement: Advertisement) {
        var counter = advertisement.viewsCounter.toInt()
        counter++
        if (auth.uid != null) {
            database
                .child(advertisement.key ?: "empty")
                .child(INFO_NODE)
                .setValue(InfoItem(counter.toString(), advertisement.emailsCounter, advertisement.callsCounter))
        }
    }

    fun onClickFavourite(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        if (advertisement.isFavourite) {
            removeFromFavourite(advertisement, finishWorkListener)
        } else {
            addToFavourite(advertisement, finishWorkListener)
        }
    }

    private fun addToFavourite(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        advertisement.key?.let {
            auth.uid?.let {
                uid -> database
                .child(it)
                .child(FAVOURITE_NODE)
                .child(uid)
                .setValue(uid)
                .addOnCompleteListener {
                    if (it.isSuccessful) finishWorkListener.onFinish() }
            }
        }
    }

    private fun removeFromFavourite(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        advertisement.key?.let {
            auth.uid?.let {
                    uid -> database
                .child(it)
                .child(FAVOURITE_NODE)
                .child(uid)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) finishWorkListener.onFinish() }
            }
        }
    }

    fun getMyAdverts(readDataCallback: ReadDataCallback?) {
        val query = database.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getMyFavouriteAdverts(readDataCallback: ReadDataCallback?) {
        val query = database.orderByChild("/${FAVOURITE_NODE}/${auth.uid}").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun deleteMyAdvert(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        if (advertisement.key == null || advertisement.uid == null) return
        database
            .child(advertisement.key)
            .child(advertisement.uid)
            .removeValue()
            .addOnCompleteListener {
            if (it.isSuccessful) finishWorkListener.onFinish()
        }
    }

    fun getAllAdverts(readDataCallback: ReadDataCallback?) {
        val query = database.orderByChild(auth.uid + "/ad/price")
        readDataFromDb(query, readDataCallback)
    }

    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val advertList = ArrayList<Advertisement>()
                for (advert in snapshot.children) {
                    var ad: Advertisement? = null
                    advert.children.forEach {
                        if (ad == null) ad = it.child(AD_NODE).getValue(Advertisement::class.java)
                    }
                    val infoItem = advert.child(INFO_NODE).getValue(InfoItem::class.java)
                    val favouriteCounter = advert.child(FAVOURITE_NODE).childrenCount
                    val isFavourite = auth.uid?.let { advert.child(FAVOURITE_NODE).child(it).getValue(String()::class.java) }
                    ad?.isFavourite = isFavourite != null
                    ad?.favouriteCounter = favouriteCounter.toString()
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailsCounter = infoItem?.emailsCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null) advertList.add(ad!!)
                }
                readDataCallback?.readData(advertList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface ReadDataCallback {
        fun readData(advertList: ArrayList<Advertisement>)
    }

    interface FinishWorkListener {
        fun onFinish()
    }

    companion object {
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
        const val FAVOURITE_NODE = "favourite"
        const val AD_NODE = "ad"
    }
}