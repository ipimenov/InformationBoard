package ru.ipimenov.informationboard.database

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.ipimenov.informationboard.data.Advertisement

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

    fun readDataFromDb() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val ad = i.children.iterator().next().child("ad").getValue(Advertisement::class.java)
                    Log.d("MyLog", "Data: ${ad?.country}")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}