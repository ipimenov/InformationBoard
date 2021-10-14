package ru.ipimenov.informationboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ipimenov.informationboard.model.Advertisement
import ru.ipimenov.informationboard.model.DbManager

class FirebaseViewModel: ViewModel() {

    private val dbManager = DbManager()
    val advertLiveData = MutableLiveData<ArrayList<Advertisement>>()

    fun loadAllAdverts() {
        dbManager.readDataFromDb(object : DbManager.ReadDataCallback {
            override fun readData(advertList: ArrayList<Advertisement>) {
                advertLiveData.value = advertList
            }
        })
    }
}