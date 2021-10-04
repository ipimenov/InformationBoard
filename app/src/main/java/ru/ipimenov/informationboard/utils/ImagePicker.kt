package ru.ipimenov.informationboard.utils

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ipimenov.informationboard.activities.EditAdsActivity

object ImagePicker {

    const val MAX_IMAGE_COUNTER = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998

    private var job: Job? = null

    fun getImages(context: AppCompatActivity, imageCounter: Int, requestCode: Int) {
        val options = Options.init()
            .setRequestCode(requestCode)                       //Request code for activity results
            .setCount(imageCounter)                                        //Number of images to restrict selection count
            .setFrontfacing(false)                                         //Front Facing camera on start
            .setMode(Options.Mode.Picture)                                 //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
            .setPath("/pix/images")                                        //Custom Path For media Storage

        Pix.start(context, options)
    }

    fun showSelectedImages(requestCode: Int, resultCode: Int, data: Intent?, editAdsActivity: EditAdsActivity) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {
            if (data != null) {
                val imageUriStrings =
                    data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (editAdsActivity.imageListFragment == null) {
                    if (imageUriStrings?.size!! > 1) editAdsActivity.openImageListFragment(imageUriStrings)
                    if (imageUriStrings.size == 1) {
                        job = CoroutineScope(Dispatchers.Main).launch {
                            editAdsActivity.binding.progressBarVp.visibility = View.VISIBLE
                            val bitmapList = ImageManager.resizeImages(imageUriStrings)
                            editAdsActivity.binding.progressBarVp.visibility = View.GONE
                            editAdsActivity.imagesViewPagerAdapter.update(bitmapList)
                        }
                    }
                } else {
                    if (imageUriStrings != null) {
                        editAdsActivity.imageListFragment?.updateAdapter(imageUriStrings)
                    }
                }
            }
        }
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE) {
            if (data != null) {
                val uriImageStrings =
                    data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                editAdsActivity.imageListFragment?.setSingleImage(uriImageStrings?.get(0)!!, editAdsActivity.editImagePosition)
            }
        }
    }
}