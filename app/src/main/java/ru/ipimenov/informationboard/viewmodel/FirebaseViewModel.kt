package ru.ipimenov.informationboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ipimenov.informationboard.model.Advertisement
import ru.ipimenov.informationboard.model.DbManager

class FirebaseViewModel: ViewModel() {

    private val dbManager = DbManager()
    val advertLiveData = MutableLiveData<ArrayList<Advertisement>>()

    fun loadAllAdverts() {
        dbManager.getAllAdverts(object : DbManager.ReadDataCallback {
            override fun readData(advertList: ArrayList<Advertisement>) {
                advertLiveData.value = advertList
            }
        })
    }

    fun loadMyAdverts() {
        dbManager.getMyAdverts(object : DbManager.ReadDataCallback {
            override fun readData(advertList: ArrayList<Advertisement>) {
                advertLiveData.value = advertList
            }
        })
    }

    fun deleteMyAdvert(advertisement: Advertisement) {
        dbManager.deleteMyAdvert(advertisement, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = advertLiveData.value
                updatedList?.remove(advertisement)
                advertLiveData.postValue(updatedList)
            }
        })
    }
}