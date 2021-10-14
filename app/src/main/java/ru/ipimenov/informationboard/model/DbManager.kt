package ru.ipimenov.informationboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {

    val database = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(advertisement: Advertisement) {
        if (auth.uid != null) {
            database
                .child(advertisement.key ?: "empty")
                .child(auth.uid!!)
                .child("ad")
                .setValue(advertisement)
        }
    }

    fun readDataFromDb(readDataCallback: ReadDataCallback?) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {

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
}