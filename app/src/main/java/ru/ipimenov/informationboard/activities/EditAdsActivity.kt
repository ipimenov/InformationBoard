package ru.ipimenov.informationboard.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.utility.PermUtil
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.adapters.ImagesViewPagerAdapter
import ru.ipimenov.informationboard.databinding.ActivityEditAdsBinding
import ru.ipimenov.informationboard.dialogs.SpinnerDialogHelper
import ru.ipimenov.informationboard.fragments.CloseFragment
import ru.ipimenov.informationboard.fragments.ImageListFragment
import ru.ipimenov.informationboard.utils.CityHelper
import ru.ipimenov.informationboard.utils.ImagePicker

class EditAdsActivity : AppCompatActivity(), CloseFragment {

    var imageListFragment: ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private val dialog = SpinnerDialogHelper()
    lateinit var imagesViewPagerAdapter: ImagesViewPagerAdapter
    var editImagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImagePicker.showSelectedImages(requestCode, resultCode, data, this)
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
                    ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
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
        imagesViewPagerAdapter = ImagesViewPagerAdapter()
        binding.vpImages.adapter = imagesViewPagerAdapter
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
        if (imagesViewPagerAdapter.imageList.size == 0) {
            ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
        } else {
            openImageListFragment(null)
            imageListFragment?.updateAdapterFromEdit(imagesViewPagerAdapter.imageList)
        }
    }

    override fun onCloseFragment(imageList: ArrayList<Bitmap>) {
        binding.svEditAds.visibility = View.VISIBLE
        imagesViewPagerAdapter.update(imageList)
        imageListFragment = null
    }

    fun openImageListFragment(linkImageList: ArrayList<String>?) {
        imageListFragment = ImageListFragment(this, linkImageList)
        binding.svEditAds.visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, imageListFragment!!)
            .commit()
    }
}