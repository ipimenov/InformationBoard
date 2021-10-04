package ru.ipimenov.informationboard.fragments

import android.graphics.Bitmap

interface CloseFragment {

    fun onCloseFragment(imageList: ArrayList<Bitmap>)
}