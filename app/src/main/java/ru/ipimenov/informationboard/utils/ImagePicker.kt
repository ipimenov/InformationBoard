package ru.ipimenov.informationboard.utils

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
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

    private fun getOptions(imageCounter: Int): Options {
        val options = Options.init()
            .setCount(imageCounter)                                        //Number of images to restrict selection count
            .setFrontfacing(false)                                         //Front Facing camera on start
            .setMode(Options.Mode.Picture)                                 //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
            .setPath("/pix/images")                                        //Custom Path For media Storage
        return options
    }

    fun launch(editAdsActivity: EditAdsActivity, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int) {
        PermUtil.checkForCamaraWritePermissions(editAdsActivity) {
            val intent = Intent(editAdsActivity, Pix::class.java).apply {
                putExtra("options", getOptions(imageCounter))
            }
            launcher?.launch(intent)
        }

    }

    fun getLauncherForMultiSelectImages(editAdsActivity: EditAdsActivity): ActivityResultLauncher<Intent> {
        return editAdsActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val imageUriStrings =
                        result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    if (editAdsActivity.imageListFragment == null) {
                        if (imageUriStrings?.size!! > 1) editAdsActivity.openImageListFragment(
                            imageUriStrings
                        )
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
        }
    }

    fun getLauncherForSingleSelectImage(editAdsActivity: EditAdsActivity): ActivityResultLauncher<Intent> {
        return editAdsActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                if (it.data != null) {
                    val imageUriString = it.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    editAdsActivity.imageListFragment?.setSingleImage(imageUriString?.get(0)!!, editAdsActivity.editImagePosition)
                }
            }
        }
    }
}