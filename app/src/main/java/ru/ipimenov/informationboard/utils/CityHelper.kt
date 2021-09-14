package ru.ipimenov.informationboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

object CityHelper {

    fun getAllCountries(context: Context): ArrayList<String> {
        var tempArrayList = ArrayList<String>()
        try {
            val inputStream = context.assets.open("countriesToCities.json")
            val size = inputStream.available()
            val byteArray = ByteArray(size)
            inputStream.read(byteArray)
            val jsonFile = String(byteArray)
            val jsonObject = JSONObject(jsonFile)
            val countryNames = jsonObject.names()

            if (countryNames != null) {
                for (i in 0 until countryNames.length()) {
                    tempArrayList.add(countryNames.getString(i))
                }
            }
        } catch (e: IOException) {

        }
        return tempArrayList
    }

    fun getAllCities(selectedCountry: String, context: Context): ArrayList<String> {
        var tempArrayList = ArrayList<String>()
        try {
            val inputStream = context.assets.open("countriesToCities.json")
            val size = inputStream.available()
            val byteArray = ByteArray(size)
            inputStream.read(byteArray)
            val jsonFile = String(byteArray)
            val jsonObject = JSONObject(jsonFile)
            val cityNames = jsonObject.getJSONArray(selectedCountry)
            for (i in 0 until cityNames.length()) {
                tempArrayList.add(cityNames.getString(i))
            }
        } catch (e: IOException) {

        }
        return tempArrayList
    }

    fun filterListData(list: ArrayList<String>, searchText: String): ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()
        for (selection: String in list) {
            if (selection.lowercase().startsWith(searchText.lowercase())) {
                tempList.add(selection)
            }
        }
        if (tempList.size == 0) tempList.add("Ничего не найдено")
        return tempList
    }
}