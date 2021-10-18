package ru.ipimenov.informationboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {

    val database = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(advertisement: Advertisement, finishWorkListener: FinishWorkListener) {
        if (auth.uid != null) {
            database
                .child(advertisement.key ?: "empty")
                .child(auth.uid!!)
                .child("ad")
                .setValue(advertisement)
                .addOnCompleteListener {
                    finishWorkListener.onFinish()
                }
        }
    }

    fun getMyAdverts(readDataCallback: ReadDataCallback?) {
        val query = database.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun deleteMyAdvert(advertisement: Advertisement, listener: FinishWorkListener) {
        if (advertisement.key == null || advertisement.uid == null) return
        database.child(advertisement.key).child(advertisement.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
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
                    val ad = advert.children.iterator().next().child("ad")
                        .getValue(Advertisement::class.java)
                    if (ad != null) advertList.add(ad)
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
}