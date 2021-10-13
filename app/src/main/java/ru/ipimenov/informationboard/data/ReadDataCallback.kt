package ru.ipimenov.informationboard.data

interface ReadDataCallback {
    fun readData(advertList: List<Advertisement>)
}