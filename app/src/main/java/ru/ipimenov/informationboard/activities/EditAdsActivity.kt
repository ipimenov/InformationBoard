package ru.ipimenov.informationboard.activities

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.databinding.ActivityEditAdsBinding
import ru.ipimenov.informationboard.dialogs.SpinnerDialogHelper
import ru.ipimenov.informationboard.utils.CityHelper
import ru.ipimenov.informationboard.MainActivity

import com.fxn.pix.Pix

import android.content.pm.PackageManager

import com.fxn.utility.PermUtil
import ru.ipimenov.informationboard.utils.ImagePicker
import android.R.attr.data

import android.app.Activity
import android.util.Log
import ru.ipimenov.informationboard.fragments.CloseFragment
import ru.ipimenov.informationboard.fragments.ImageListFragment


class EditAdsActivity : AppCompatActivity(), CloseFragment {

    lateinit var binding: ActivityEditAdsBinding
    private val dialog = SpinnerDialogHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {
            if (data != null) {
                val returnValue =
                    data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (returnValue?.size!! > 1) {
                    binding.svEditAds.visibility = View.GONE

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.place_holder, ImageListFragment(this, returnValue))
                        .commit()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 3)
//                    Pix.start(context, Options.init().setRequestCode(100))
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun init() {
    }

    // OnClicks
    fun onClickSelectCountry(view: View) {
        val listOfCountries = CityHelper.getAllCountries(this)
        dialog.spinnerDialogShow(this, listOfCountries, binding.tvCountry)
        if (binding.tvCity.text.toString() != getString(R.string.select_city)) {
            binding.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View) {
        val selectedCountry = binding.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listOfCities = CityHelper.getAllCities(selectedCountry, this)
            dialog.spinnerDialogShow(this, listOfCities, binding.tvCity)
        } else {
            Toast.makeText(this, "Не выбрана страна", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickGetImages(view: View) {
        ImagePicker.getImages(this, 3)

//        ImagePicker.getImages(this)
    }

    override fun onCloseFragment() {
        binding.svEditAds.visibility = View.VISIBLE
    }
}